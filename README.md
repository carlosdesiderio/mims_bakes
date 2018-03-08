
# Baking App Project
Udacity Android Nanodegree Project THREE submission

## Project Specification

The project aims is to create an application for master baker Miriam to share her recipes 
with the world. Users will be able to select a recipe and view a video-guided steps to guide them
through the process. They will also be able to add ingredients to a shopping list .

Full rubric can be found [here](https://review.udacity.com/#!/rubrics/829/view)
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

All the data requests and consequent response parsing is done 
asynchronously with the use of the Retrofit library

#### Data Strategy
A single request is made to the Baking endpoint at the application startup. The 
data is deserialiased 
and stored in a local database. No further network request are made throughout the application 
lifespan.


#### Data Persistence
The data received from the Recipe Service Request is persisted in a local [SQLite database](app/src/main/java/uk/me/desiderio/mimsbakes/data/BakesDBHelper.java) The 
database has a structure with the following tables:  

* **Recipes** - holds data about recipes. 
* **Ingredients** - holds data about the recipe ingredients. The table has a one-to-many 
relationship with the recipes table which is enforced with the use of a foreign key referring 
back to the recipe that ingredient belongs to.
* **Steps** - holds data about the recipe instruction steps. The table has a one-to-many relationship with the recipes table which is enforced with the use of a foreign key referring back to the recipes that it belongs to.
* **Shopping** - holds data about the ingredients in the shopping list. It has a one-to-one 
relationship both with the ingredients table and the recipes table 
which is
 enforced with the use a double foreign key referring back to these tables respectably. 

The data is made available to the rest of the application with the implementation of a Provider. 
The [BakesContentProvider](app/src/main/java/uk/me/desiderio/mimsbakes/data/BakesContentProvider.java) supports the following actions:
* insert : recipes, ingredients in shopping table
* bulk insert : ingredients, steps and ingredients in shopping table
* delete : deletes ingredients from the shopping table
* query: all tables

A **_outer join selection statement_** is used to query data of the ingredient 
present in the shopping list. 
Ingredient details are gathered from the ingredients table 
every time a ingredient is present in the shopping table.

The provider also implements a technique where it registers its interest to 
database changes and notifies them to the ContentResolver so that the loaders are notified as soon the changes occur.

## UI 
### The App
The application follows the master-detail view pattern consisting of three views:
* Master view showing a list of recipes. This is implemented in the [MainActivity](app/src/main/java/uk/me/desiderio/mimsbakes/MainActivity.java).
* Details view showing the ingredients and step instructions of the recipe selected in the master view. This is implemented in the [RecipeDetailsFragment](app/src/main/java/uk/me/desiderio/mimsbakes/RecipeDetailsFragment.java). The steps list is in itself a Master View where a step can be selected.
* Recipe step details view showing a video and description of the step instructions. It is 
implemented in the [StepVideoFragment](app/src/main/java/uk/me/desiderio/mimsbakes/StepVideoFragment.java).

![Alt text](readme_files/mims_diagram_port.png?raw=true "Default master detail flow")

As part of the project requirements, the app implements a adaptive design where the recipe details and step instruction details are shown as part of the same view when using a tablet in landscape orientation. The default journey shows these two screens as a two distinct views as it can be above

![Alt text](readme_files/mims_diagram_land.png?raw=true "Tablet landscape master detail flow")

The recipe list provides different layouts for the different screen sizes. It shows one column 
for small devices, two columns for tablet portrait and 3 columns for tablet landscape.

As said, 
two 
pane 
layout is use in tablets 
while
 in 
landscape orientation. This involves a transition from StepVideoActivity to  
RecipeDetailsActivity when rotating from portrait. In this case, the app makes
 sure to keep state and shows the same instruction step in both activities after the 
 transition.

#### The shopping list
The user could add any ingredient to a shopping list while browsing a 
recipe. Ingredients can be added individually by selecting the ingredient itself. An
 option menu item is provided to add / remove all recipe ingredients at 
 once. 

When an ingredient is added or removed to/from the shopping list, the user is 
prompted with a message. Also any ingredient in the shopping list will 
appear highlighted  in the recipe ingredient list.

#### Exoplayer
The project uses version of the exoplayer library version 2.6.1. 
This was the latest version at the time of production. 
This is a different version from the one used in the examples and 
required a different implementation to the one shown there.

The player provides a simple and easy to use interface where the instruction video can be easily 
paused while in the process of following a recipe. The player starts immediately as soon the step is
 selected. Video playback can be paused and restarted by touching anywhere in the player. The 
 video restarts playback from the beginning when the user touches the screen after the video is ended.

The SimpleExoplayerView’s controls have been customised so that to only show the progress of the 
content. Any other functions have been removed. The controls shows a timebar and two text 
views for this purpose.
 
The player provides the following feedback to the user:
* buffering 
* video is paused
* video is finished prompting the user to reply video
* current progress of the video

At a implementation level, the player doesn’t make used of a MediaSession due
 to its scale. One player is created per fragment that is instantiated. The player
  is released when the fragment is detached from its 
  activity. Error handling is not implemented at a UI level at this stage.
### The Widget
The application has a companion homescreen widget which displays a
ingredient list for any of the recipes available.

Selecting any of the ingredients listed in the widget will open the selected ingredient recipe 
where the ingredient could then be removed/added from the list.

The widget displays a message when the shopping list is 
empty. Selecting this message will open the app at the main activity (recipe 
list)


## Testing
### Unit Testing
The project includes unit test for the request and parsing of the data. It 
also provides unit test for the database and model classes.
### Espresso Testing
The project provides Espresso tests for the MainActivity:
[MainActivityTest](app/src/androidTest/java/uk/me/desiderio/mimsbakes/MainActivityTest.java) 

In order to assist to these tests a idling resource, a view assertion 
and customised matchers have been implemented
* [RecipeIdlingResource](app/src/main/java/uk/me/desiderio/mimsbakes/espresso/RecipeIdlingResource.java) 
* [RecyclerViewCountAssertion](app/src/androidTest/java/uk/me/desiderio/mimsbakes/RecyclerViewCountAssertion.java) 
* [RecyclerViewCountAssertion](app/src/androidTest/java/uk/me/desiderio/mimsbakes/MatcherUtils.java) 

The [Parameterized runner](http://junit.sourceforge.net/javadoc/org/junit/runners/Parameterized.html) is used to run these tests with a set of values  

### Libraries
This is a list of the 
external libraries use in the project as requirement:

* [square / retrofit](https://github.com/square/retrofit)
* [square / picasso](https://github.com/square/picasso)
* [hdodenhof / CircleImageView](https://github.com/hdodenhof/CircleImageView)

For a list of support and other Android libraries, see the 
[build.gradle](app/build.gradle)file

### Contact:
labs@desiderio.me.uk

