
# baking app project
Udacity Android Nanodegree Project submissions THREE

## Getting Started

The project aims is to create an application for renouned baker Miriam to share her recipes 
with the world. Users will be able to select a recipe and see video-guided steps for how to 
complete it. They will also be able to add ingredients to the shopping list .

## Implementation
As a requirement, this project is written using only Java.

#### Network
The application retrieves the baking data from a single endpoint with the following schema:

```
[
  {
    "id": 1,
    "name": "",
    "ingredients": [
      {
        "quantity": 2,
        "measure": "",
        "ingredient": ""
      },
      …
    ],
    "steps": [
      {
        "id": 0,
        "shortDescription": "",
        "description": "",
        "videoURL": "",
        "thumbnailURL": ""
      },
      …
    ],
    "servings": 0,
    "image": ""
   },
  …
]
```
See full JSON response in this [here](https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json) 

All the data requests and consequent parsing of their response is done asynchronously with the use of the Retrofit library

#### Data Strategy
A single request is done to the Baking endpoing at application startup. The data is desirialiesed 
and stored in a local database. No futher network request are made throughtout the application 
lifespan.


#### Data Persistence
The data received from the Recipe Service Request is persisted in a local [SQLite database](app/src/main/java/uk/me/desiderio/mimsbakes/data/BakesDBHelper.java) The 
database has a structure with the following tables:  

* **Recipe** - holds data about the recipe  that has been received from he network request. 
* **Ingredients** - holds data about the recipe ingredients. The table has a one-to-many 
relationship with the recipes table which is enforced with the use of a foreign key referring back to the recipe that it belongs to.
* **Steps** - holds data about the recipe instruction steps. The table has a one-to-many relationship with the recipes table which is enforced with the use of a foreign key referring back to the recipes that it belongs to.
* **Shopping** - holds data about the ingredients in the shopping list. It has a one-to-one relationship with the ingredients list and one-to-many relationship with the recipes table which is enforced with the use a double foreign key referring back to these tables respectably. 

The data is made available to the rest of the application with the implementation of a Provider. 
The [BakesContentProvider](app/src/main/java/uk/me/desiderio/mimsbakes/data/BakesContentProvider.java) supports the following actions:
* insert : recipes, ingredients in shopping table
* bulk insert : ingredients, steps and ingredients in shopping table
* delete : deletes ingredients from the shopping table
* query: all tables

A outer join selection statement is used to query data of the ingredient present in the shopping list. 
Ingredient details are gathered from the ingredients table 
every time the ingredient is present in the shopping table.

The provider also implements a technique where it registers interest to the database changes and notifies them to the ContentResolver so that the loaders are notified as soon the changes occur.


## UI 
The application follows the master-detail view pattern consisting of three views:
* Master view showing a list of recipes. This is implemented in the MainActivity 
* Details view showing the ingredients and step instructions of the recipe selected in the master view. This is implemented in the RecipeDetailsFragment. The steps list is in itself a Master View where a step can be selected.
* Recipe step details view showing a video and description of the step instructions. It is implemented in the StepVideoFragment

![Alt text](readme_files/mims_diagram_port.png?raw=true "Default master detail flow")

As part of the project requirements, the app implements a adaptive design where the recipe details and step instruction details are shown as part of the same view when using a tablet in landscape orientation. The default journey shows these two screens as a two distinct views as it can be above

![Alt text](readme_files/mims_diagram_land.png?raw=true "Tablet landscape master detail flow")

### Contact:
labs@desiderio.me.uk

