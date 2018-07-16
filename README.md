# ClojureScript setup - some notes

Below are some notes concerning ClojureScript setup including setting up of Leiningen and Speclj.

## Hello World in ClojureScript - basics

In order to learn basics of ClojureScript, I've followed this guide: https://clojurescript.org/guides/quick-start.
Below I've copied some code from the guide and added my own comments.

#### Setup project structure

```plain
hello-world        # Our project folder
├─ src             # The CLJS source code for our project
│  └─ hello_world  # Our hello_world namespace folder
│     └─ core.cljs # Our main file
└─ deps.edn        # (macOS/Linux only) A file for listing our dependencies
```

deps.edn should contain:

```dependencies
{:deps {org.clojure/clojurescript {:mvn/version "1.10.339"}}}
```

core.cljs should contain:

```ClojureScript
(ns hello-world.core)

(println "Hello world!")
```
#### Run the program

In the command line enter:

```plain
clj --main cljs.main --compile hello-world.core --repl
```
--main invokes a Clojure function, in this case cljs.main. The cljs.main function supports a variety of command line arguments to specify common tasks.

We’re using --compile to specify that we want to compile the hello-world.core namespace.

This is followed by --repl to say that we want a REPL to launch immediately when compilation completes.


In order to run repl only, do:
```plain
 clj --repl
```

#### Change core.cljs

```ClojureScript
(ns hello-world.core) // namespace

(println "Hello world!")


(defn average [a b]
  (/ (+ a b) 2.0))
```

Last two lines is:
- definition of a method average that takes two arguments "a" and "b".
- we add a and b (+ a b) then we divide them (/) by 2.0.

#### Running in repl, reloading a file

At the REPL prompt, recompile and reload your namespace by evaluating the following:
(require '[hello-world.core :as hello] :reload)
Here, the whole namespace hello-world.core will be named as hello. From now on, when we want to use sth from the namespace, we will just call hello.

When we add :reload, it will run the program. Without it, it will just require it (eg. the methods will be available to use) but without running it.

Then we call an average method from hello namespace and pass two arguments to it.

```plain
(hello/average 20 13)
```

#### Run a built in simple web server via the --serve flag:

Run:
```plain
clj --main cljs.main --compile hello-clojureS.core --serve
```
Open localhost:9000 and google dev tools to see it printed.

## Setup of Leiningen

I was advised to use https://github.com/slagyr/speclj testing framework that looks and feels a lot like RSPec (ruby testing framework). I order to use it, I needed Leiningen - a tool for automating Clojure and ClojureScript projects. The installation process is described here: https://github.com/technomancy/leiningen/blob/stable/README.md. I'll briefly address some of the steps that caused me some problems.

Step 2 - Download the lein script from the stable branch of this project.

I've downloaded the script from here: https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein. But then I had no idea how and where to save it. I've decided to save it on desktop with a lein name, no file extension.

Step 3 - Place it on your ```$PATH```. (```~/bin``` is a good choice if it is on your path.)

Placing something on your ```$PATH``` means that you won't have to set the path to the executable / script, but you can run it by its name as a command, for example node app.js, npm init etc.

How to know what the path is? Type echo ```$PATH``` in the terminal to learn. For me the output is:

```plain
/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin
```

I've tried to move my lein file from the desktop to /usr/bin/ (mv lein /usr/bin/) but it was not permitted, even with sudo command. It was probably some security measure on my computer. I've decided then to move it to /usr/local/bin/ which is also on the path.

Step 4 - Set it to be executable. ```(chmod +x ~/bin/lein)```.
I needed to change lein file permissions to be executable by all groups.

Once you're done with the steps, enter lein help in the terminal - it will install leiningen automatically.

P.S. You can install leiningen by using homebrew (```brew install leiningen```) - but you risk having an older/unofficial version + don't get the important lesson of using ```$PATH```.

## Starting a speclj project with a testing framework

Once I've installed leiningen, I've came back to setting up speclj project. I run:

```plain
lein new specljs testing_project
```

to create a testing_project. As a result, I got a new testing_project folder.

Pay attention to two directories inside of it: spec and src. Both have two folders clj and cljs - clj will be for Clojure code, while cljs for ClosureScript. In spec I'll put my test code, in src al other code.

In the root there is also project.clj file which configures the project. It defines dependencies, plugins and paths.

in the bin folder there is a speclj file - a test runner executable.

Now, let's take a look at core_spec.cljs file where we can find the following code:

```ClojureScript
(ns first_project.core-spec
  (:require-macros [speclj.core :refer [describe it should=]])
  (:require [speclj.core]
            [first_project.core :as my-core]))

(describe "A ClojureScript test"
  (it "fails. Fix it!"
    (should= 0 1)))
```
This is our first failing test.

First, we `:require-macros` the `speclj.core` namespace and `:refer` each speclj test word that you want to use.
Your spec files must :require the speclj.core too. It loads all the needed speclj namespaces. Also pull in the library that you're testing (first_project.core in this case).

In order to test our code, we also need to require our namespace (code from src folder) - in the case above I've aliased it using :as.

In order to run it, I run from the root level:

```plain
$ lein cljsbuild test
```
what gave me a failing test as I wanted:

#### Common problems bonus :)
The two problems that I've happened upon at this stage were:
- speclj didn't work with Java 10 - needed to downgrade to Java 8.
- I didn't have phantomJS - I've installed it using homebrew (`brew install phantomjs`). You can also do it manually (downloading & adding to `/usr/local/bin/` as described above for lein).

#### Testing methods of my authorship
Finally I've tried to test a method of my authorship - I've used my average method created above and pasted in to the `src/cljs/first_project/core.cljs`:

```ClojureScript
(ns first_project.core)

(defn average [a b]
(/ (+ a b) 2.0))
```

I wrote for it the following passing test and added it to the spec/cljs/first_project/core_spec.cljs file:

```ClojureScript
(describe "Counts average"
  (it "returns average"
    (should=
      15
      (my-core/average 20 10))))
```
Bang, passing!
