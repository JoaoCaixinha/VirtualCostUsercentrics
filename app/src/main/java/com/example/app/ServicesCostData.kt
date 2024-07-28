package com.example.app

enum class DataCostTable constructor(val dataCollected: String){
    // ON TABLE
    CONFIGURATION_OF_APP_SETTINGS("Configuration of app settings "),
    IP_ADDRESS("IP address"),
    USER_BEHAVIOUR("User behaviour"),
    USER_AGENT("User agent"),
    APP_CRASHES("App crashes"),
    BROWSER_INFORMATION("Browser information"),
    CREDIT_DEBIT_CARD_NUMBER("Credit and debit card number"),
    FIRST_NAME("First name"),
    GEOGRAPHIC_LOCATION("Geographic location"),
    DATE_TIME_OF_VISIT("Date and time of visit"),
    ADVERTISING_IDENTIFIER("Advertising identifier"),
    BANK_DETAILS("Bank details"),
    PURCHASE_ACTIVITY("Purchase activity"),
    INTERNET_SERVICE_PROVIDER("Internet service provider"),
    JAVASCRIPT_SUPPORT("JavaScript support"),

    // EXTRA
    SEARCH_TERMS("Search terms");


    companion object {
        fun getEnumFromDataCollectedString(data: String): DataCostTable? {
            return DataCostTable.values().find { it.dataCollected == data }
        }

        fun getIncrementCost(data: DataCostTable): Int {
            return when (data) {
                IP_ADDRESS, USER_BEHAVIOUR, ADVERTISING_IDENTIFIER -> 2
                USER_AGENT, BROWSER_INFORMATION -> 3
                APP_CRASHES -> -2
                BANK_DETAILS, INTERNET_SERVICE_PROVIDER -> 5
                FIRST_NAME, PURCHASE_ACTIVITY -> 6
                GEOGRAPHIC_LOCATION -> 7
                JAVASCRIPT_SUPPORT -> -1
                CONFIGURATION_OF_APP_SETTINGS -> 1
                CREDIT_DEBIT_CARD_NUMBER -> 4
                DATE_TIME_OF_VISIT -> 1
                SEARCH_TERMS -> 0
            }
        }

        fun getExtraPercentageCost(data: DataCostTable): Double {
            return when (data) {
                PURCHASE_ACTIVITY, BANK_DETAILS, CREDIT_DEBIT_CARD_NUMBER -> 1.10
                SEARCH_TERMS, GEOGRAPHIC_LOCATION, IP_ADDRESS -> 1.27
                else -> 1.0
            }
        }

        fun getBonusPercentage(cost:Double, count: Int): Double {
            return when {
                count <= 4 -> cost - (cost * 0.10)
                else -> cost
            }
        }

    }
}