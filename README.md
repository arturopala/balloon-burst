Balloon Burst
===

Motivation
----

An exercise in reactive functional programming.

Specification
---

The purpose of balloon burst is to measure propensity to risk, in it you must inflate a balloon as many times as you dare and stop when you think
it has been inflated as much as it will allow. If you manage to inflate a balloon and bank it you get the number of inflations added to your score, if
you inflate the balloon beyond it's limit it will burst, and you don't get to bank anything.

- First input is a list of balloon inflate limits eg: 10 2 4 6 9 2 with each number representing a balloon, and it's value the maximum number of
inflations it can withstand,
- Then an input of INFLATE will inflate the current balloon, an input of BANK will bank the current score and move on to the next balloon,
- If the number of inflate attempts goes over that balloons burst limit a BURST must be printed to the user, input commands after that go to the
next balloon,
- Once there are no balloons left the final score is printed to the console.

Run
---

    sbt run
    
Example
---

    $ sbt run
    -> 2 4 1
    -> INFLATE
    -> INFLATE
    -> INFLATE
    <- BURST
    -> INFLATE
    -> BANK
    -> INFLATE
    -> BANK
    <- SCORE: 2    