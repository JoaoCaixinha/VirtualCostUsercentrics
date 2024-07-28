package com.example.app


const val DEFAULT_EXTRA: Double = 1.0
const val MIN_BONUS_VALUE: Int = 4

enum class DataCostTable(val dataCollected: String){
    // ON TABLE
    CONFIGURATION_OF_APP_SETTINGS("Configuration of app settings "),
    USER_BEHAVIOUR("User behaviour"),
    USER_AGENT("User agent"),
    APP_CRASHES("App crashes"),
    BROWSER_INFORMATION("Browser information"),
    FIRST_NAME("First name"),
    DATE_TIME_OF_VISIT("Date and time of visit"),
    ADVERTISING_IDENTIFIER("Advertising identifier"),
    INTERNET_SERVICE_PROVIDER("Internet service provider"),
    JAVASCRIPT_SUPPORT("JavaScript support"),

    // EXTRA
    PURCHASE_ACTIVITY("Purchase activity"),
    BANK_DETAILS("Bank details"),
    CREDIT_DEBIT_CARD_NUMBER("Credit and debit card number"),

    SEARCH_TERMS("Search terms"),
    IP_ADDRESS("IP address"),
    GEOGRAPHIC_LOCATION("Geographic location");

    companion object {
        fun getEnumFromDataCollectedString(data: String): DataCostTable? {
            return DataCostTable.values().find { it.toString() == data }
        }

        fun getIncrementCost(data: DataCostTable): Int {
            return when (data) {
                IP_ADDRESS, USER_BEHAVIOUR, ADVERTISING_IDENTIFIER -> 2
                USER_AGENT, BROWSER_INFORMATION -> 3
                APP_CRASHES -> -2
                BANK_DETAILS -> 5
                FIRST_NAME, PURCHASE_ACTIVITY -> 6
                GEOGRAPHIC_LOCATION -> 7
                JAVASCRIPT_SUPPORT -> -1
                CONFIGURATION_OF_APP_SETTINGS -> 1
                CREDIT_DEBIT_CARD_NUMBER, INTERNET_SERVICE_PROVIDER -> 4
                DATE_TIME_OF_VISIT -> 1
                SEARCH_TERMS -> 0
            }
        }

        private fun getExtraPercentageCost(data: DataCostTable, onExtraFound:(Double)->Unit): Unit {
            return when (data) {
                PURCHASE_ACTIVITY, BANK_DETAILS, CREDIT_DEBIT_CARD_NUMBER -> onExtraFound(0.10)
                SEARCH_TERMS, GEOGRAPHIC_LOCATION, IP_ADDRESS -> onExtraFound(0.27)
                else -> {}
            }
        }

        fun getBonusPercentage(cost:Double, count: Int): Double {
            return when {
                count <= MIN_BONUS_VALUE -> cost - (cost * 0.10)
                else -> cost
            }
        }

        fun calculateCost(data: String, initialCost: Double, onExtraFound:(Double)->Unit): Double {
            var cost = initialCost
            val dataCollected = DataCostTable.getEnumFromDataCollectedString(data)
            // if dataCollected is null, the string in dataCollectedList is not in the enum DataCostTable.
            // on the exercise there no specification how to handle the situation
            dataCollected?.let {
                // Table increment
                cost += DataCostTable.getIncrementCost(it)

                // Rule 1: Why do you care?
                // Rule 2: Why do you care?
                DataCostTable.getExtraPercentageCost(it, onExtraFound)
            }

            return cost
        }

    }
}