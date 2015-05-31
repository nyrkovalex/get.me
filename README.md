# get.me
Git based java software installer.

Goal of this project is to let java developers install opensource tools and
libraries straight from github or other git repository by one simple console command like
`get.me https://github.com/nyrkovalex/get.me.git`

Of course there should be some guidelines on how to make your software installable this way.
Currently the flow goes as follows


## Setup your environment
0. Make sure you have JDK 8 installed on your system
1. Create a directory somwhere to store executable jars
2. Set `JARPATH` environment variable to that directory
3. Download a [run.me script](https://gist.github.com/nyrkovalex/abc66911f6c1e227f76b)
 and put it somewhere under your `$PATH`
4. Download [get.me.jar](https://github.com/nyrkovalex/get.me/releases/download/v0.1-alpha/get.me.jar)
and put it under your `$JARPATH`
5. Test your setup trying to install let's say [this project](https://github.com/nyrkovalex/migrate.me)
by running `run.me get.me https://github.com/nyrkovalex/migrate.me`. You should see some maven build
output and if everything goes fine you'll get a `migrate.me.jar` under your `$JARPATH` directory.
You may now try running it with `run.me migrate.me`. This should print you an error message about
missing `migrate.me.json` that means everything went fine.


## Future
* The flow described above is to be automated
* Dependency resolution mechanism so one can build all dependencies with single command
* More installers and builders to come
* Docs, manuals and guides


_Contributions welcome!_ Beware that coding style on this project is rather weird for java world.
Feel free to contact me I won't bite :)
