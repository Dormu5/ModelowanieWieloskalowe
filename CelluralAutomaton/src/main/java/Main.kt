
import tornadofx.App
import kotlin.random.Random

var arrayOfGrains: Array<IntArray> = arrayOf()
var val1 = 0
var val2 = 0
var val3 = 0

class HelloWorldApp : App(Hello::class)
enum class MethodTypes{
    METHOD_30,
    METHOD_60,
    METHOD_90,
    METHOD_120,
    METHOD_225
}

fun runMethod(methodType: MethodTypes ,rows: Int,columns: Int): Array<IntArray> {
    generateArray(rows, columns)
    when (methodType) {
        MethodTypes.METHOD_30 -> {
            method30(rows,columns)
            return arrayOfGrains

        }
        MethodTypes.METHOD_60 ->  {
            method60(rows,columns)
            return arrayOfGrains
        }
        MethodTypes.METHOD_90 ->  {
            method90(rows,columns)
            return arrayOfGrains
        }
        MethodTypes.METHOD_120 ->  {
            method120(rows,columns)
            return arrayOfGrains
        }
        MethodTypes.METHOD_225 ->  {
            method225(rows,columns)
            return arrayOfGrains
        }
    }
 }
fun main() {
    val rows = 20
    val columns = 20



    customPrint(arrayOfGrains)
    method30(rows,columns)
    customPrint(arrayOfGrains)
    method60(rows,columns)
    customPrint(arrayOfGrains)
    method90(rows,columns)
    customPrint(arrayOfGrains)
    method120(rows,columns)
    customPrint(arrayOfGrains)
    method225(rows,columns)
    customPrint(arrayOfGrains)








}


fun generateArray(rows: Int, columns: Int) {


        val tempArray: Array<IntArray> = Array(rows) { IntArray(columns) { 0 } }
            tempArray[0][Random.nextInt(0,columns-1)] = 1
            arrayOfGrains = tempArray


}

fun customPrint(array: Array<IntArray>) {
    for (i in array.indices) {
        for (j in 0 until array[i].size) {
            print(array[i][j].toString() + " ")
        }
        println()
    }
    println()
    println()
    println()
}



fun method30 (rows: Int, columns: Int){
    for ( i in 1 until rows){
        for (j in 0 until columns){
            when (j) {
                0 -> {

                    val1 = arrayOfGrains[i - 1][columns - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
                columns - 1 -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][0]
                }
                else -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
            }

            if (val1 == 0 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 0 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 0 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 1 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 1 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            }

        }
    }

}

fun method60 (rows: Int, columns: Int) {
    for (i in 1 until rows) {
        for (j in 0 until columns) {
            when (j) {
                0 -> {
                    val1 = arrayOfGrains[i - 1][columns - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
                columns - 1 -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][0]
                }
                else -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
            }

            if (val1 == 0 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 0 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 1 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            }
        }


    }
}


fun method90 (rows: Int, columns: Int) {
    for (i in 1 until rows) {
        for (j in 0 until columns) {
            when (j) {
                0 -> {
                    val1 = arrayOfGrains[i - 1][columns - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
                columns - 1 -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][0]
                }
                else -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
            }

            if (val1 == 0 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 0 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 1 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            }
        }


    }
}

fun method120 (rows: Int, columns: Int) {
    for (i in 1 until rows) {
        for (j in 0 until columns) {
            when (j) {
                0 -> {
                    val1 = arrayOfGrains[i - 1][columns - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
                columns - 1 -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][0]
                }
                else -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
            }

            if (val1 == 0 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 0
            } else if (val1 == 0 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 1
            } else if (val1 == 1 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 0
            }
        }


    }
}

fun method225 (rows: Int, columns: Int) {
    for (i in 1 until rows) {
        for (j in 0 until columns) {
            when (j) {
                0 -> {
                    val1 = arrayOfGrains[i - 1][columns - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
                columns - 1 -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][0]
                }
                else -> {
                    val1 = arrayOfGrains[i - 1][j - 1]
                    val2 = arrayOfGrains[i - 1][j]
                    val3 = arrayOfGrains[i - 1][j + 1]
                }
            }

            if (val1 == 0 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 1;
            } else if (val1 == 0 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 0;
            } else if (val1 == 0 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 0;
            } else if (val1 == 0 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 0;
            } else if (val1 == 1 && val2 == 0 && val3 == 0) {
                arrayOfGrains[i][j] = 0;
            } else if (val1 == 1 && val2 == 0 && val3 == 1) {
                arrayOfGrains[i][j] = 1;
            } else if (val1 == 1 && val2 == 1 && val3 == 0) {
                arrayOfGrains[i][j] = 1;
            } else if (val1 == 1 && val2 == 1 && val3 == 1) {
                arrayOfGrains[i][j] = 1;
            }
        }


    }
}









