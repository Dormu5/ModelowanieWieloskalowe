import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.TextField
import tornadofx.*


class Hello : View("My View") {

    var numberOfRows: TextField by singleAssign()
    var numberOfColumns: TextField by singleAssign()
    val selectedMethod = SimpleStringProperty()
    val methodsNames = FXCollections.observableArrayList(MethodTypes.values().map { it.name })
    var method: MethodTypes? = null
    var resultsMatrix: Array<IntArray> = arrayOf()
    val data = FXCollections.observableArrayList<Int>()
    val cellCountObservable = SimpleObjectProperty(1)
    val maxRowsObservable = SimpleObjectProperty(1)
        val sizeOfCell = 10.0


    override val root = vbox {
        prefHeight = 1000.0
        prefWidth = 1000.0
        hbox {
            label("number of rows")
            numberOfRows = textfield()
        }
        hbox {
            label("number of columns")
            numberOfColumns = textfield()
        }
        hbox {


            combobox(selectedMethod, methodsNames)
            selectedMethod.onChange {
                method = MethodTypes.valueOf(selectedMethod.value)
            }
        }


        button("Show") {
            useMaxWidth = true
            action {
                resultsMatrix =
                    runMethod(method!!, Integer.parseInt(numberOfRows.text), Integer.parseInt(numberOfColumns.text))
                cellCountObservable.value = resultsMatrix.firstOrNull()?.count() ?: 0
                maxRowsObservable.value = resultsMatrix.count()
                data.setAll(resultsMatrix.flatMap { it.toList() })
            }
        }

        datagrid(data) {
            maxCellsInRowProperty.bind(cellCountObservable)
            maxRowsProperty.bind(maxRowsObservable)
            cellWidth = sizeOfCell
            cellHeight = sizeOfCell
            horizontalCellSpacing = 0.0
            verticalCellSpacing = 0.0

            cellFormat {

                if (it == 1)
                    graphic = vbox {

                        style {

                            backgroundColor += c("#a94442")
                        }
                    }
                else
                    graphic = vbox {

                        style {

                            backgroundColor += c("#000000")
                        }
                    }
            }

        }


    }
}
