# Rating Base Frontend

## Install
Import the project as a maven project. Then build using clean install.

**Do note that the backend must be started and initialised first**

## Library
Due to its size the library has to be downloaded from https://drive.google.com/drive/folders/18ySSj2SpodUHlvpTMRglx1NyJcmIXtk7?usp=sharing

### Set up the library repository
Put the file in project base directory

type the following command in a terminal  

    mvn install:install-file -Dfile=Sentiment-Analysis.jar -DgroupId=com.stanford_nlp -DartifactId=Sentiment-Analysis -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=lib

clean install maven
