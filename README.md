# Usercentrics SDK App Challenge

This repository contains the solution to the Usercentrics SDK App Challenge, which consists of creating an app that integrates the Usercentrics banner UI framework and implements a calculator to determine the virtual cost of giving consent to the app.
This repository as two branchs, **main** and **feature/implement-usercentrics**.
A pull request **Feature/implement usercentrics cost calculator** is created from the feature branch to main, there is available all the implementation.

## Challenge Overview
The challenge involves two main tasks:
1. Implementing the Usercentrics consent banner in the app and handling user responses.
2. Calculating the cost of giving consent based on the services and data types declared by the app.

## Tools and Resources
- Usercentrics Apps Documentation
- SettingsID: "gChmbFIdL"
- Compose for ui
- ViewModel Pattern
- LiveData for ui update
- UnitTesting


## Implementation
The solution will be implemented in Android (Kotlin), following best coding practices, ViewModel design patterns, LiveData, Observer, Compose ui build and clean code principles. The main focus will be on showcasing coding skills, knowledge of patterns, and delivering a functional solution.

## Table of Contents
1. Usercentrics Integration
2. Consent Banner Implementation
3. Calculator Logic
4. Data Type Cost Calculation
5. Cost Calculation Rules
6. Console Output
7. Bonus Feature: Display Total Cost in UI

Feel free to explore the documentation and code to understand the implementation details of each section.

---
Now, for the structure of the rest of the document:

## 1. Usercentrics Integration
Usercentrics sdk is integrated in the project acording with the steps describe in the [documentation](https://docs.usercentrics.com/cmp_in_app_sdk/latest/getting_started/install/).
The project uses the ViewModel pattern, while **VirtualCostCounterViewModel** encapsulates the business logic, **MainActivity** encapsulates the graphical interface.

1. The SDK initialization is available onCreate method of MainActivity.

``` 
val options = UsercentricsOptions(settingsId = getString(R.string.UserCentricsSettingsID))
Usercentrics.initialize(this, options)
``` 

3. VirtualCostCounterViewModel, Event detection and cost calculations 

	1. Usercentrics ready listiner event.
	2. **applyConsent** handles consent accept and calculates cost according per service and total, prints on consolog using  `Log.d(VirtualCostCounter, "${consent.dataProcessor} = $cost")`.
	3. **totalCost** liveData stores the total cost
	4. **onShowBannerPressed**, decides wich banner layer to show
	5. **virtualCostCounterEvents** viewModel emit events that MainActivity is observing	


## 2. Consent Banner Implementation
1. **VirtualCostCounterViewModel** handles on button pressed from UI on compose View `VirtualCostCounter` 

```
fun onShowBannerPressed() {
        if (_isFirstTime.value == true) {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowFirstLayer)
        } else {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowSecondLayer)
        }
    }
```
2. **Broadcast** event based if the user as acceped from frist layer, that value is store on device storage, automatic loaded on app restart. 


3. In MainActivity there are also methods responsible for presenting the banner, first and second layer respectively.

``` /*
    * Display first layer banner and returns the response to viewmodel to be handles in applyConsent
    * */
    private fun showFirstLayer() {
        val banner = UsercentricsBanner(this)
        banner.showFirstLayer() { userResponse ->
            viewModel.setUserAsFirstTime(userResponse?.userInteraction?.name == ACCEPT_ALL)
            viewModel.applyConsent(userResponse?.consents)
        }
    }

    /*
    * Display second layer banner and returns the response to viewmodel to be handles in applyConsent
    * */
    private fun showSecondLayer() {
        val banner = UsercentricsBanner(this)
        banner.showSecondLayer() { userResponse ->
            viewModel.applyConsent(userResponse?.consents)
        }
    }
``` 


## 3. Calculator Logic

For service cost handling a String enum **DataCostTable** has created.
It as the list of fields avalible on the table from the challange.

It counts with a companion object with severel methods useful for the cost calculatio.

1. `private fun getEnumFromDataCollectedString(data: String): DataCostTable?` given the dataCollected string return the enum entry if available.

2. **Rule 1** - `private fun getIncrementCost(data: DataCostTable): Int` return the int value for a given element from the enum.
3. **Rule 2** - `private fun getExtraPercentageCost` constains a list of the cases withe extra percentage for diferent lists.
4. **Rule 3** - `private fun getBonusPercentage(cost: Double, count: Int): Double` verifies if the is bonus

 ```
private fun calculate(
        data: String,
        initialCost: Double,
        onExtraFound: (Rule, Double) -> Unit
    ): Double
```
5. calculates cost and verifies if rule 1 or 2 is applicable, as not very clear in challange, assume that those rule apply over total cost.
6. ``fun calculateCost(dataCollectedList: List<String>): Double`` call calculate to calculate cost based on table values, apply rules if apllicable.


## 4. Display Total Cost in UI
The application UI as mentioned in the statement, consisting of a button to display the banner and two labels, one informative and another that represents the total cost, is implemented in the Compose view **VirtualCostCounter**,
This is invoked in the **MainActivity**.

## 5. VirtualCostCounterViewModel UnitTests list
*  `test on application starts it fetch store info if first layer was accepted`     
*  `test isFirstTime setter method is updating variable`
*  `test if isFirstTime application display first layer banner`
*  `test if not isFirstTime application display second layer banner`

## 6. DataCostTableTest calculations UnitTests list

*  `test 2 fields, none are rule 1 or rule 2, but just 2 should have bonus (rule 3)`
*  `test 3 fields, with rule 1, but just 3 should have bonus (rule 3)`
*  `test 3 fields, with rule 2, but just 3 should have bonus (rule 3)`
*  `test 5 fields, with rule 1, should not have bonus (rule 3)`
*  `test 6 fields, with rule 1 and 2, should not have bonus (rule 3)`
*  `test 2 fields, with rule 1 or rule 2, but just 2 should have bonus (rule 3)`
*  `test 2 fields from rule 1, should multiply one time, and apply bonus (rule 3)`
*  `test 2 fields from rule 2, should multiply one time, and apply bonus (rule 3)`()






