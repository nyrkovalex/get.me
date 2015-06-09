# Get.Me

Git based java software builder and installer.


# What is it for?

Goal of this project is to let java developers install opensource tools and
libraries straight from github or other git repository by one simple console command like
`get.me https://github.com/nyrkovalex/get.me.git`


# How _should_ it work?

The flow is pretty trivial:

0. Find some interesting tool or library hosted at github
1. Run `get.me https://github.com/someone/something`
2. You're done


# Why?

While developing in Java I was very jealous of the ease JavaScript developers have installing 
various cli tools like `jhsint` or `pm2`. They don't have to download tons of compiled binaries or jars,
setup some environment variables, write shell scripts or do some other boring stuff they just need
to type `npm -g install jshint` and they're done! 

Java community partly solves library hosting problem with [Maven Central](http://search.maven.org/)
which is indeed great but not every developer wants to host his or her jars there, to comply
with packaging guidelines, have no non-mvn-central dependencies. Someones' company may have
guidelines that restrict one from using public repositories and there may be 1K more reasons
not to use MvnCentral. 

But as far as I know there's no chance to host your CLI or GUI java tool 
so that it can be one command away from user's OS, we're just screwed. The only option left is
to create an archive maintaining some internal structure and instruct a user to go to `./bin`
directory there and to run `sh my-tool.sh` or `.cmd` or whatever.
 
So the goal of this project is to make your software installable with one console command while
enabling you to use all the power of tools you're used to like Maven, Ant, Gradle or event Bash.

Of course there should be some guidelines on how to make your software installable this way.


# How can I install something?

Currently the flow goes as follows


## Setup your environment

0. Make sure you have JDK 8 installed on your system
1. Create a directory somwhere to store executable jars
2. Set `JARPATH` environment variable to that directory
3. Download a [run.me script](https://gist.github.com/nyrkovalex/abc66911f6c1e227f76b) and put it somewhere
under your `$PATH`. This fellow is about to run any jar under `$JARPATH` with one command
4. Download [get.me.jar](https://github.com/nyrkovalex/get.me/releases/download/v0.1-alpha2/get.me.jar)
and put it under your `$JARPATH`. This is the first and non-stable version of Get.Me which will be able to
update itself later
5. Test your setup trying to install let's say [this project](https://github.com/nyrkovalex/migrate.me)
by running `run.me get.me https://github.com/nyrkovalex/migrate.me`. You should see some maven build
output and if everything goes fine you'll get a `migrate.me.jar` under your `$JARPATH` directory.
You may now try running it with `run.me migrate.me`. This should print you an error message about
missing `migrate.me.json` that means everything went fine.


# How can I make my software installable by Get.Me?

0. Create a `get.me.json` file under your project's repository root
1. Describe your desired builder and installer with their parameters (those are plugin-specific).
Descriptor format is really simple
```json
[
  {
    "class": "com.github.someone.BuilderClass",
    "params": {
      "plugin-specific stuff here": true
    }
  },
  {
    "class": "com.github.someone.InstallerClass",
    "params": {
      "plugin-specific stuff here": true
    }
  }
]
```


# Future

* The flow described above is to be automated
* Dependency resolution mechanism so one can build all dependencies with single command
* More installers and builders to come especially Installer capable of installing Builder
and Installer plugins
* Docs, manuals and guides

# How may I help?

0. There's an [issue list](https://github.com/nyrkovalex/get.me/issues) which contains recent
issues. Fork, code, send me a pull request — I won't bite :)
1. Feedback, suggestions, ideas, pretty much anything
