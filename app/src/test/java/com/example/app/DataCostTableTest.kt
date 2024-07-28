package com.example.app

import org.junit.Assert.assertEquals
import org.junit.Test

class DataCostTableTest {
    @Test
    fun `test 2 fields, none are rule 1 or rule 2, but just 2 should have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.USER_AGENT.toString()) // 3
        dataCollectedList.add(DataCostTable.INTERNET_SERVICE_PROVIDER.toString()) // 4

        var cost = DataCostTable.calculateCost(dataCollectedList)
        assertEquals(6.3, cost, 0.1)
    }

    @Test
    fun `test 3 fields, with rule 1, but just 3 should have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.USER_AGENT.toString()) // 3
        dataCollectedList.add(DataCostTable.INTERNET_SERVICE_PROVIDER.toString()) // 4
        dataCollectedList.add(DataCostTable.BANK_DETAILS.toString()) // 5

        var cost = DataCostTable.calculateCost(dataCollectedList)
        // (12 * 0.10) 13.2
        assertEquals(11.88, cost, 0.1)
    }

    @Test
    fun `test 3 fields, with rule 2, but just 3 should have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.USER_AGENT.toString()) // 3
        dataCollectedList.add(DataCostTable.INTERNET_SERVICE_PROVIDER.toString()) // 4
        dataCollectedList.add(DataCostTable.IP_ADDRESS.toString()) // 2

        var cost = DataCostTable.calculateCost(dataCollectedList)
        assertEquals(10.287, cost, 0.1)
    }

    @Test
    fun `test 5 fields, with rule 1, should not have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.USER_AGENT.toString()) // 3
        dataCollectedList.add(DataCostTable.INTERNET_SERVICE_PROVIDER.toString()) // 4
        dataCollectedList.add(DataCostTable.BANK_DETAILS.toString()) // 5
        dataCollectedList.add(DataCostTable.APP_CRASHES.toString()) // -2
        dataCollectedList.add(DataCostTable.JAVASCRIPT_SUPPORT.toString()) // -1

        var cost = DataCostTable.calculateCost(dataCollectedList)
        assertEquals(9.9, cost, 0.1)
    }

    @Test
    fun `test 6 fields, with rule 1 and 2, should not have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.USER_AGENT.toString()) // 3
        dataCollectedList.add(DataCostTable.INTERNET_SERVICE_PROVIDER.toString()) // 4
        dataCollectedList.add(DataCostTable.BANK_DETAILS.toString()) // 5
        dataCollectedList.add(DataCostTable.APP_CRASHES.toString()) // -2
        dataCollectedList.add(DataCostTable.JAVASCRIPT_SUPPORT.toString()) // -1
        dataCollectedList.add(DataCostTable.SEARCH_TERMS.toString()) // -1

        var cost = DataCostTable.calculateCost(dataCollectedList)
        assertEquals(12.33, cost, 0.1)
    }

    @Test
    fun `test 2 fields, with rule 1 or rule 2, but just 2 should have bonus (rule 3)`() {
        val dataCollectedList = ArrayList<String>()
        dataCollectedList.add(DataCostTable.BANK_DETAILS.toString()) // 5
        dataCollectedList.add(DataCostTable.GEOGRAPHIC_LOCATION.toString()) // 7

        var cost = DataCostTable.calculateCost(dataCollectedList)
        assertEquals(14.796, cost, 0.1)
    }

}