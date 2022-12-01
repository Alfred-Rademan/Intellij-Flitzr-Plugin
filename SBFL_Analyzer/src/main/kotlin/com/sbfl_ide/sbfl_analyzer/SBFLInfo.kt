package com.sbfl_ide.sbfl_analyzer

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import java.io.File

class SBFLInfo {
    var entries = ArrayList<SBFLEntry>()

    fun parseSBFL(e: AnActionEvent) {
        entries.clear()
        var lines: List<String> = File(e.project?.basePath+"/flitsr.txt").readLines()
        var sbflLine: Int = 0
        var name: String = ""
        var sbflVal: Double = 0.0

        for (line in lines){
            var info = line.split(" ")
            if(info[0].equals("Faulty")){
                sbflVal = info[3].toDouble()
            } else if (info[0].get(0) == '(') {
                name = info[2].substring(info[2].indexOf("$") + 1,info[2].indexOf("#"))
               sbflLine  = info[6].toDouble().toInt() - 1
                var entry = SBFLEntry(name,sbflVal,sbflLine)
                entries.add(entry)
            }
        }
    }

    //@Leeshen if you enter the name of a class this returns a list of all info about that class (see SBFLEntry for
    // the format of the entries)
    // If you want to get the current open file in the editors name use

    // var vFile = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
    // var name = vFile!!.nameWithoutExtension

    // this gives the current file name. Note that e is AnActionEvent
    fun getClassInfo(name: String) : ArrayList<SBFLEntry>{
        var classEntries = ArrayList<SBFLEntry>()

        for (entry in entries) {

            if (entry.file == name) {
                classEntries.add(entry)
            }

        }

        return classEntries
    }

    fun getAverage(sbflArray: ArrayList<SBFLEntry>) : Double {
        var avrg = 0.0

        for (entry in sbflArray) {
            avrg += entry.sbflVal
        }
        avrg /= sbflArray.size

        return avrg
    }

    fun getMax(sbflArray: ArrayList<SBFLEntry>) : Double {
        var m = 0.0
        for (entry in sbflArray) {
            m = maxOf(m,entry.sbflVal)
        }
        return m
    }

}