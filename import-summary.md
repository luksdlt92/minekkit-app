ECLIPSE ANDROID PROJECT IMPORT SUMMARY
======================================

Ignored Files:
--------------
The following files were *not* copied into the new Gradle project; you
should evaluate whether these are still needed in your project and if
so manually move them:

* .gitignore
* ic_launcher-web.png
* proguard-project.txt

Moved Files:
------------
Android Gradle projects use a different directory structure than ADT
Eclipse projects. Here's how the projects were restructured:

* AndroidManifest.xml => app\src\main\AndroidManifest.xml
* assets\ => app\src\main\assets\
* libs\apache-mime4j-core-0.7.2.jar => app\libs\apache-mime4j-core-0.7.2.jar
* libs\httpclient-4.3.5.jar => app\libs\httpclient-4.3.5.jar
* libs\httpcore-4.3.2.jar => app\libs\httpcore-4.3.2.jar
* libs\httpmime-4.3.5.jar => app\libs\httpmime-4.3.5.jar
* res\ => app\src\main\res\
* src\ => app\src\main\java\

Next Steps:
-----------
You can now build the project. The Gradle project needs network
connectivity to download dependencies.

Bugs:
-----
If for some reason your project does not build, and you determine that
it is due to a bug or limitation of the Eclipse to Gradle importer,
please file a bug at http://b.android.com with category
Component-Tools.

(This import summary is for your information only, and can be deleted
after import once you are satisfied with the results.)