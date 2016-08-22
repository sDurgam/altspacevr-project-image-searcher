# AltspaceVR Programming Project - Android Image Searcher

Image Searcher Android app fetches and displays different images using [Pixabay image API][pixabay].

## Features
Displays popular, latest, saved and editors choice sections 

  * Support for browsing images in each section
   
  * Support for browsing different sections using scrollable tabs
   
  * Support for saving or deleting images displayed in each section for offline browsing by clicking on Save/Unsave option on the image
   
  * Support to browse more images as by scrolling down the images each section
   
  * Support for searching images by tags in each section

## Functionality

* Implemented view pager and tab layout in the MainActivity. Popular, latest, saved and editors choice sections are displayed as tabs and each tab can be browsed using the view pager
 
* Created `PopularFragment`, `LatestFragment`, `SavedFragment` and `EditorsChoiceFragment`. These fragments extend PhotoFragment fragment. Functionality to browse relevant images and search by tags is implemented in each of these fragments.
 
* Implemented `itemsListRecyclerView.setOnScrollListener()` in every fragment. This method fetches relevant images by passing page number as an argument to Pixabay image API as user scrolls down the images displayed in each section.
 
* Created `SearchPhotosEvent` event with query and type as arguments. query can be a string to search or null. type is 'popular', 'latest', 'save' or 'editorschoice. Main Activity posts SearchPhotosEvent when user submits query in the SearchView in any section or when user closes the SearchView. PopularFragment, LatestFragment, SavedFragment and EditorsChoiceFragment are subscribed to this event. Relevant code to fetch images by search query is handled in each of these fragments based on the type argument.
 
* Created `UpdateSavedEvent` event. PopularFragment, LatestFragment, SavedFragment and EditorsChoiceFragment are subscribed to this event. This event is invoked when a new image is saved or a saved image is deleted by the user. This event updates the save status of the image displayed in these fragments.
 
* Created `ItemsListAdapter`, `ItemsSavedListAdapter` adapters. They both extend ItemsBaseAdapter adapter. Code to display image and save status of each photo, save photo into database or delete saved photo from the database and invoke UpdateSavedEvent when user clicks on 'Save' or 'Unsave' option on the image is handled in these adapters.
 
* Implemented the CRUD operations in `DatabaseUtil` so photo information can be queried, saved, and loaded from a local db.
 
* Implemented `ApiService.getPopularPhotos`, `ApiService.getLatestPhotos`, `ApiService.getEditorsChoicePhotos` methods. These methods call the relevant Pixabay APIs to fetch images. Pagenumber is passed as an argument to these methods. Pixabay returns 20 photos for each page.
 
* Implemented `ApiService.searchPhotos`, `ApiService.searchLatestPhotos`, `ApiService.searchEditorsChoicePhotos` methods. These methods call the relevant Pixabay APIs to fetch images matching the search query. Pagenumber is passed as an argument to these methods. Pixabay returns 20 photos for each page.
 
* Implemented test cases to test activity life cycle, test if view pager, tablayout and fragments are rendered. Test case to populate PopularFragment and perform click operation on the save button of the image.
 
* Implemented test cases to test database connection, CRUD operations.

## New features in UI

- TabLayout, view pager, floating Action Button to display Pixabay logo

- `Snackbar` to display messages in the following cases:
	* User is not connected to the network
	* No results are found for the search query user submitted using SearchView
	* Photo could not be saved to database whern user clicks on save option of the photo

## New libraries used

`Robolectric` to implement test cases

##Future Enhancements

- Support for displaying image in full screen mode

- Support for sharing image via Share option

- Support for setting image as wallpaper

- Support for landscape screen mode 

[pixabay]:https://pixabay.com/api/docs/
