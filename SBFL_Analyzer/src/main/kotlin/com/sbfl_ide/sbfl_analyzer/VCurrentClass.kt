package com.sbfl_ide.sbfl_analyzer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PiePlot
import org.jfree.data.general.DefaultPieDataset
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JFrame

/*
VCurrent Class displays a pie chart (making using of the JFreeChart library)
of the percentages of the current open class in the editor that are Critical
Working and Faulty
 */

private var red = 0
private var green = 0
private var yellow = 0
private var f = JFrame("Visualisation for Current Class")
private var length = 0
class VCurrentClass: AnAction() {

/*
fun actionPerformed takes in the action event of clicking
Current class in the SBFL Dropdown menu, it makes use of the
SBFLInfo class and its fun parseSBFL() to retrieve the name
of the current class and then fun getClassInfo() to get all
information of the current class which it then uses to
calculate the different groupings
 */
    override fun actionPerformed(e: AnActionEvent) {
        red = 0
        green = 0
        yellow = 0
        f = JFrame("Visualisation for Current Class")
        val currentclass = SBFLInfo()
        currentclass.parseSBFL(e)
        val vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
        val name = vFile!!.nameWithoutExtension
        println(name)
        val classes : ArrayList<SBFLEntry> = currentclass.getClassInfo(name)
        length = classes.size
        for (entry in classes) {
            if (entry.sbflVal > 100.0) {
                red++
            } else if (entry.sbflVal == 0.0) {
                green++
            } else {
                yellow++
            }
        }
        /*
        for (i in intarray) {
            println(i)
            if (i > 100) {
                red++
                continue
            }
            if (i == 0) {
                green++
            } else {
                yellow++
            }
        }
        */
        createUI()
        createAndShowGUI()


    }
/*
fun getClassVals converts the ArrayList<SBFLEntry> of fun getClassInfo()
into an IntArray containg just the sbfl values
 */
    fun getClassVals(classes: ArrayList<SBFLEntry>): IntArray {

    val vArray = IntArray(length)
    val x = 0

    for (entry in classes) {
        vArray[x] = entry.sbflVal.toInt()
    }
    return vArray
}

/*
fun createDataset creates the Default dataset required as one
of the parameters to create a pie chart by implementing the
interface in JFreeChart. Entries are KeyedValue
*/
    fun createDataset(): DefaultPieDataset<String>{
        val dataset = DefaultPieDataset<String>()
        dataset.setValue("Critical", red)
        dataset.setValue("Working", green)
        dataset.setValue("Faulty", yellow)
        return dataset
    }

/*
fun createChart takes in the dataset created by the method above
and creates the actual pie chart
*/
    fun createChart(dataset: DefaultPieDataset<String>): JFreeChart {
        return ChartFactory.createPieChart("", dataset, false, true, false)
    }
/*
fun createsUI() creates all the UI elements necessary to display the graph
it also custom sets the colors of the pie chart segments to match the Key
meanings
 */
    fun createUI() {
        val dataset = createDataset()
        val chart = createChart(dataset)
        val plot = chart.plot as PiePlot<*>
        plot.setSectionPaint("Critical", Color.red)
        plot.setSectionPaint("Working", Color.green)
        plot.setSectionPaint("Faulty", Color.yellow)
        val chartPanel = ChartPanel(chart)
        chartPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
        chartPanel.background = Color.white
        f.add(chartPanel)

        f.pack()
        f.title = "Visualisation for Current Class"
        f.setLocationRelativeTo(null)
        f.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
    }

/*
    fun createAndShowGUI() just sets the JFrame to visible
 */
    fun createAndShowGUI() {
        f.isVisible = true
    }
/*
fun update() which checks if there is a file open in the editor so that the Visualisations option
can be clicked from the dropdown
 */
    override fun update(e: AnActionEvent) {
        var project = e.project
        var editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = (project != null && editor != null)
    }
}

