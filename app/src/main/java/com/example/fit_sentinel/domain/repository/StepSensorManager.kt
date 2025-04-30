package com.example.fit_sentinel.domain.repository

// StepDetector.kt
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * كلاس مسؤول عن اكتشاف الخطوات باستخدام مستشعر التسارع والجيروسكوب.
 * يعتمد على خوارزمية Complementary Filter لدمج البيانات.
 */
class StepDetector(context: Context) : SensorEventListener {

    // region ### 1. إعداد المتغيرات والإعدادات الأولية ###

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // المستشعرات
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    // معاملات الخوارزمية
    private val alpha = 0.98f  // وزن الجيروسكوب في الـ Complementary Filter
    private var threshold = 1.5f  // العتبة لاكتشاف الخطوة (تعدل تلقائيًا إذا لم يوجد جيروسكوب)
    private val minStepInterval = 400L  // أقل زمن بين خطوتين (ملي ثانية)

    // متغيرات التتبع
    private var lastStepTime = 0L
    private var stepCount = 0
    private var smoothedMagnitude = 0f
    private var roll = 0f  // الميل الجانبي (Roll)
    private var pitch = 0f  // الميل الأمامي (Pitch)
    private var lastGyroData = FloatArray(3)  // آخر قراءة للجيروسكوب
    private var isGyroAvailable = false  // هل الجيروسكوب متوفر؟

    // endregion

    // region ### 2. تهيئة المستشعرات ###
    init {
        // التحقق من وجود المستشعرات
        isGyroAvailable = gyroscope != null

        if (accelerometer == null) {
            Log.e("Sensors", "accelormeter not available")
        } else {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }

        if (isGyroAvailable) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME)
        } else {
            Log.w("Sensors", "Gyroscope not available, falling back to accelerometer ")
            threshold = 1.8f  // زيادة العتبة لتعويض نقص الدقة
        }
    }
    // endregion

    // region ### 3. معالجة بيانات المستشعرات ###
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> handleAccelerometer(event)
            Sensor.TYPE_GYROSCOPE -> handleGyroscope(event)
        }
    }

    // معالجة بيانات الجيروسكوب
    private fun handleGyroscope(event: SensorEvent) {
        System.arraycopy(event.values, 0, lastGyroData, 0, 3)
    }

    // معالجة بيانات التسارع
    private fun handleAccelerometer(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()

        // 3.1 حساب الزوايا من الجيروسكوب (إذا كان متوفرًا)
        if (isGyroAvailable) {
            val deltaTime = (currentTime - lastStepTime).toFloat() / 1000f
            updateOrientationWithGyro(deltaTime)
        }

        // 3.2 حساب الزوايا من التسارع
        val (rollAcc, pitchAcc) = calculateOrientationFromAccel(event)

        // 3.3 دمج الزوايا باستخدام Complementary Filter
        roll = alpha * roll + (1 - alpha) * rollAcc
        pitch = alpha * pitch + (1 - alpha) * pitchAcc

        // 3.4 إزالة تأثير الجاذبية
        val linearAccel = removeGravity(event.values, roll, pitch)

        // 3.5 اكتشاف الخطوة
        detectStep(linearAccel, currentTime)
    }

    // endregion

    // region ### 4. دوال مساعدة ###
    // تحديث الزوايا باستخدام الجيروسكوب
    private fun updateOrientationWithGyro(deltaTime: Float) {
        roll += Math.toDegrees(lastGyroData[1].toDouble()).toFloat() * deltaTime
        pitch += Math.toDegrees(lastGyroData[0].toDouble()).toFloat() * deltaTime
    }

    // حساب الزوايا من التسارع
    private fun calculateOrientationFromAccel(event: SensorEvent): Pair<Float, Float> {
        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]

        val rollAcc = atan2(ay, az).toDegrees()
        val pitchAcc = atan2(-ax, sqrt(ay * ay + az * az)).toDegrees()

        return Pair(rollAcc, pitchAcc)
    }

    // إزالة الجاذبية من التسارع
    private fun removeGravity(accel: FloatArray, roll: Float, pitch: Float): FloatArray {
        val g = SensorManager.STANDARD_GRAVITY
        val gx = g * sin(pitch.toRadians())
        val gy = -g * sin(roll.toRadians())
        val gz = g * cos(pitch.toRadians()) * cos(roll.toRadians())

        return floatArrayOf(
            accel[0] - gx,
            accel[1] - gy,
            accel[2] - gz
        )
    }

    // اكتشاف الخطوة بناءً على مقدار التسارع
    private fun detectStep(linearAccel: FloatArray, currentTime: Long) {
        val sumSquares = linearAccel.fold(0f) { acc, value ->
            acc + (value * value)
        }
        val magnitude = sqrt(sumSquares)
        //val magnitude = sqrt(linearAccel.sumOf { it * it }.toFloat())
        smoothedMagnitude = alpha * smoothedMagnitude + (1 - alpha) * magnitude

        if (smoothedMagnitude > threshold && (currentTime - lastStepTime) > minStepInterval) {
            stepCount++
            lastStepTime = currentTime
            Log.d("StepDetector", "A step is detected , count: $stepCount")
        }
    }

    // تحويل الزاوية من راديان إلى درجة
    private fun Float.toDegrees() = Math.toDegrees(this.toDouble()).toFloat()

    // تحويل الزاوية من درجة إلى راديان
    private fun Float.toRadians() = Math.toRadians(this.toDouble()).toFloat()

    // endregion

    // region ### 5. إدارة دورة حياة الكلاس ###
    fun getStepCount(): Int = stepCount

    fun resetStepCount() {
        stepCount = 0
    }

    fun destroy() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}  // غير مستخدم هنا
    // endregion
}