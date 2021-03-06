package iris.json.flow

import iris.json.IrisJson
import iris.json.IrisJsonItem
import iris.json.IrisJsonNull
import java.lang.Appendable

/**
 * @created 20.09.2020
 * @author [Ivan Ivanov](https://vk.com/irisism)
 */
class FlowValue(tokener: Tokener) : FlowItem(tokener) {

    private val data: Tokener.PrimitiveData by lazy(LazyThreadSafetyMode.NONE) { this.tokener.readPrimitive() }

    override fun toString(): String {
        return data.sequence.toString() + "[" + data.type + "]"
    }

    override fun <A : Appendable> joinTo(buffer: A): A {
        buffer.append(data.sequence)
        return buffer
    }

    override fun get(ind: Int): IrisJsonItem {
        return IrisJsonNull.Null
    }

    override fun get(key: String): IrisJsonItem {
        return IrisJsonNull.Null
    }

    private fun init(): Any? {
        val s = data.sequence.toString()
        return when (data.type) {
            IrisJson.ValueType.Constant -> when (s) {
                "null" -> null
                "true" -> true
                "false" -> false
                else -> s
            }
            IrisJson.ValueType.Integer -> s.toLong()
            IrisJson.ValueType.Float -> s.toDouble()
            else -> throw IllegalArgumentException("No argument: ${data.type}")
        }
    }

    private val ready by lazy(LazyThreadSafetyMode.NONE) { init() }

    override fun obj(): Any? {
        return ready
    }

    override fun parse() {
        data
    }
}