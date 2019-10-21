package org.carder.sample

import org.junit.Test
import kotlin.math.sqrt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val target = 12f
        val result = sqrt(target)
        println(result)
        if (result.toInt() * result.toInt() == target.toInt()) {
            println("成功")
        } else {
            println("失败")
        }
    }
}
