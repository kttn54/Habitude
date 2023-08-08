# Habitude - Habit Tracking App

Habitude is a modern, simple, and efficient habit tracking app, developed with the latest Android development practices. This app enables users to track their habits, encouraging them to maintain productive routines to achieve their goals.

## Demonstration

![Habitude_gif](https://github.com/kttn54/Habitude/assets/127300104/6972a180-37b6-4bbe-8146-5afc3f165192)

## Features

* User Authentication with Firebase.
* Adding and managing habits.
* Displaying habit progress with rich UI.
* Sending password reset emails.
* Daily automatic habit updates with background processing.
* And much more...

## Tech Stack

The following tech stack was used to develop Habitude:

* Kotlin - The project is entirely written in Kotlin, including the use of Coroutines and Flow for asynchronous tasks.
* Architecture Components - Lifecycle-aware components, ViewModels, LiveData, and Navigation.
* Firebase - For storing user data and managing user authentication.
* Hilt - For dependency injection.
* WorkManager - For scheduling periodic work like updating habits data every day.
* Material Components - For implementing Material Design.

## Code Structure

The project follows the MVVM design pattern. It has a solid separation of concerns, and it makes use of ViewModels and data source classes, providing a clean codebase and architecture.

## How To Run

1. Clone this repository
2. Update the `gradle.properties` with your Firebase configuration.
3. Run the application using Android Studio and an emulator.

## Testing

The project has both instrumentation tests (located in the `androidTest` directory) and unit tests (located in the `test` directory). They can be run respectively with `./gradlew connectedCheck` and `./gradlew test` in your terminal.

## Contact 

If you need any help, you can connect with me.

Visit -> [My Github](https://github.com/stporg/)

## Acknowledgments

Thanks to everyone who has helped me in this journey.
