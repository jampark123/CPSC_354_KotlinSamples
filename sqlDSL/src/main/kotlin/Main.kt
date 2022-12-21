import org.jetbrains.annotations.TestOnly
import java.lang.IllegalArgumentException
import java.util.concurrent.locks.Condition

class SqlSelectBuilder{
    private val columns = mutableListOf<String>()
    private  lateinit var table: String

    fun from(table: String){
        this.table = table
    }

    fun build(): String {
        if(!::table.isInitialized){
            throw IllegalArgumentException("Failed to build an sql select - target table is undefined")
        }
        return toString()
    }

    override fun toString(): String {
        val columnsToFetch =
            if(columns.isEmpty()) {
                "*"
            } else {
                columns.joinToString {", "}
            }
        return "select $columnsToFetch from $table"
    }

    fun select(vararg columns: String) {
        if (columns.isEmpty()){
            throw IllegalArgumentException("At least one column should be defined")
        }
        if (this.columns.isNotEmpty()){
            throw IllegalArgumentException("Detected an attempt to redefine columns to fetch. "
                + "Current columns list: "
                + "${this.columns}, new columns list: $columns")
        }
        this.columns.addAll(columns)
    }

    fun where(initializer: Condition.() -> Unit){
        condition = And().apply(initializer)
    }
}

fun query(initializer: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    from("myTable")
    where{
        "column3" eq 4
        "column3" eq null
    }
    return SqlSelectBuilder().apply(initializer)
}

abstract class Condition {
    infix fun String.eq(value: Any?){
        addCondition(Eq(this, value))
    }

    fun and(initializer: Condition.() -> Unit) {
        addCondition(And().apply(initializer))
    }

    fun or(initializer: Condition.() -> Unit) {
        addCondition(Or().apply(initializer))
    }
}

class And : Condition() {

    private val conditions = mutableListOf<Condition>()

    override fun addCondition(condition: Condition) {
        conditions += condition
    }

    override fun toString(): String {
        return if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(prefix = "(", postfix = ")", separator = " and ")
        }
    }
}

class Eq(private val column: String, private val value: Any?) : Condition() {

    init {
        if (value != null && value !is Number && value !is String) {
            throw IllegalArgumentException(
                "Only <null>, numbers and strings values can be used in the 'where' clause")
        }
    }

    override fun addCondition(condition: Condition) {
        throw IllegalStateException("Can't add a nested condition to the sql 'eq'")
    }

    override fun toString(): String {
        return when (value) {
            null -> "$column is null"
            is String -> "$column = '$value'"
            else -> "$column = $value"
        }
    }
}

class And : Condition() {

    private val conditions = mutableListOf<Condition>()

    override fun addCondition(condition: Condition) {
        conditions += condition
    }

    override fun toString(): String {
        return if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(prefix = "(", postfix = ")", separator = " and ")
        }
    }
}

open class CompositeCondition(private val sqlOperator: String) : Condition() {
    private val conditions = mutableListOf<Condition>()

    override fun addCondition(condition: Condition) {
        conditions += condition
    }

    override fun toString(): String {
        return if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(prefix = "(", postfix = ")", separator = " $sqlOperator ")
        }
    }
}

class And : CompositeCondition("and")

class Or : CompositeCondition("or")


/*
query {
    select("column1", "column2")
    from ("myTable")
}
 */

/*
private fun doTest(expected: String, sql: SqlSelectBuilder.() -> Unit) {
    assertThat(query(sql).build()).isEqualTo(expected)
}

@TestOnly
fun `when no columns are specified then star is used`() {
    doTest("select * from table1") {
        from ("table1")
    }
}
@TestOnly
fun `when no condition is specified then correct query is built`() {
    doTest("select column1, column2 from table1") {
        select("column1", "column2")
        from ("table1")
    }
}

@TestOnly
fun `when a list of conditions is specified then it's respected`() {
    doTest("select * from table1 where (column3 = 4 and column4 is null)") {
        from ("table1")
        where {
            "column3" eq 4
            "column4" eq null
        }
    }
}

@TestOnly
fun `when 'or' conditions are specified then they are respected`() {
    doTest("select * from table1 where (column3 = 4 or column4 is null)") {
        from ("table1")
        where {
            or {
                "column3" eq 4
                "column4" eq null
            }
        }
    }
}

@TestOnly
fun `when either 'and' or 'or' conditions are specified then they are respected`() {
    doTest("select * from table1 where ((column3 = 4 or column4 is null) and column5 = 42)") {
        from ("table1")
        where {
            or {
                "column3" eq 4
                "column4" eq null
            }
            "column5" eq 42
        }
    }
}
*/

/*
class SqlSelectBuilder {
    fun build(): String {
        TODO("implement")
    }
}
*/

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}