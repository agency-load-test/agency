package com.onit.statistic

import com.onit.agent.Agent
import com.onit.configuration.Configuration
import kscience.plotly.*
import kscience.plotly.models.ScatterMode
import java.time.format.DateTimeFormatter

object Graphing {

    fun plotGraph(agents: List<Agent>) {
        val data = calculateXY(agents)

        val plot = Plotly.plot {
            scatter {
                x.set(data.first)
                y.set(data.second)
                name = "Endpoint Response Times"
                mode = ScatterMode.markers
            }

            layout {
                title = "Endpoint Response Times"
                xaxis {
                    title = "Progress"
                }
                yaxis {
                    title = "Response Times"
                }
            }
        }

        val fileName = Configuration.getOutputFile()
        plot.makeFile(fileName)
        println("Plot created at $fileName")
    }

    private fun calculateXY(agents: List<Agent>): Pair<List<String>, List<Double>> {
        val x =
            agents.map { it.requestDurationStatistic }.flatMap { it.toList() }.sortedBy { it.time }.map { it.time }.map { it.format(
                DateTimeFormatter.ISO_DATE_TIME) }
        val y =
            agents.map { it.requestDurationStatistic }.flatMap { it.toList() }.sortedBy { it.time }.map { it.value }
        return Pair(x, y)
    }
}