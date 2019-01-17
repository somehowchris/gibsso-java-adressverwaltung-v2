# Project Adressverwaltung
>   A semi Äpel production class application
## Setup
### Build & Code
This Project is managed by maven. To build the project you need to have a internet connection or the dependencies cached locally. In order to run the project you would need a stable release of java 1.8
### Database & Fs
Configurations are stored in a ```.env``` file in the current users root folder. These settings can be accesses by the Settings tab in the application. If you would like to manage them manually please encode them in UTF-8 with the following needed keys
```
DATABASE_NAME = Your database name right here
DATABASE_HOST = Your host ip or domain right here
DATABASE_PORT = Your database port right here
DATABASE_USER = Your database username right here
DATABASE_PASSWORD = Your password of the database user right here
DATABASE_USE = if you wanna have a db set it to true else set it to false
```
## Deploy
### Fat file
To deploy this Application to customers we implemented a fat file version which will need Java installed but includes all the specified dependencies within. Just build and clean the application and in the target directory of the project a file called ```Adressverwaltung-jar-with-dependencies.jar``` will show up which is the one.
### External libraries
In order to use external libraries please use the ```Adressverwaltung.jar``` which seems tiny bu all the oder folders are needed in the root directory of the jar file or globally known to java. Please make sure you're running a environment with all the libraries in the globally used by java or excecute the jar file within the folder where all the other folders a placed in.


[![forthebadge](https://forthebadge.com/images/badges/gluten-free.svg)](https://forthebadge.com)	[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)	[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)	[![forthebadge](https://forthebadge.com/images/badges/powered-by-oxygen.svg)](https://forthebadge.com)
