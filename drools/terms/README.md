# Terminalogies

## Rule unit
- a class that implements `RuleUnitData`
- which groups data sources, global variables and DRL rules
    - `DataStore<UserDefinedInputClass>` --> data sources
        - An `UserDefinedInputClass` object is called a **Fact**
    - Any variable within the class
- a new and recommended style for implementing rules in Drools 8

## Data sources
- strongly typed sources of data that rule units can subscribe to for updates
- 2 kinds: 
    - `DataStream`: append-only
    - `DataStore`: writable (adding/removing then notify all subscriberss)
    - `SingletonStore`: A writable storage option for setting or clearing a single element and then notifying all subscribers that the element has been modified. Works as global var
- DataSources can be shared across different units

## Data processors
- subscribers to a data source
- implements `DataProcessor<T>` as event handlers

## KieBase
- a repository of all the application’s knowledge definitions
- it contains rules, processes, functions, and type models
- itself does not contain data; instead, sessions are created from the KieBase into which data can be inserted and from which process instances may be started

## KieSession
- it stores and executes on the runtime data
- created from KieBase

### default KieSession
- An empty kmodule.xml will produce a single (default) KieBase that includes all files found under resources path
- Default means they can be created without knowing their names.
- vs **Named KieSession** defined in kmodule.xml

## KieRuntime
- it provides methods that are applicable to both rules and processes, such as setting globals and registering channels
