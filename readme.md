[ ![Download](https://api.bintray.com/packages/lamartio/maven/LiveDataUtils/images/download.svg) ](https://bintray.com/lamartio/maven/LiveDataUtils/_latestVersion)
[![Code Coverage](https://img.shields.io/badge/coverage-94%25-green.svg)](https://github.com/Lamartio/LiveDataUtils/tree/master/library/src/test/java/io/lamart/livedata/utils)
[![Gitter](https://badges.gitter.im/LiveDataUtils/community.svg)](https://gitter.im/LiveDataUtils/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)
# LiveData utils
When a ViewModel has to work with more complex logic and state, then the default operators  of LiveData (`map`, `switchMap` and `distinctUntilchanged`) are not enough. Hence this repo introduces the most popular operators from Rx, but then as extension functions on LiveData.

There is a sample project included for demonstration.

```
dependencies {
    implementation 'io.lamart.livedata:livedata-utils:+'
    // ...
}
```

## Creating LiveData

``` kotlin
val d1 : MutableLiveData<Int> = mutableLiveDataOf(1)
val d2 : LiveData<Int> = liveDataOf { next ->
    next(1)
    next(2)
    next(3)
}
val d3 : MutableLiveData<Int> = mediatorLiveDataOf {
    addSource(mutableLiveDataOf(), ::setValue)
    addSource(mutableLiveDataOf(), ::postValue)
}

```

## Transforming LiveData
``` kotlin
mutableLiveDataOf(1).filter { it % 2 == 0 } // filters the odd numbers

mutableLiveDataOf(1).cast<Int>() // blindly cast each value
mutableLiveDataOf(1).castIf<String>() // cast only when it is possible

mutableLiveDataOf(1).take(3) // only the first three are emitted
mutableLiveDataOf(1).skip(3) // skips the first 3 emissions
mutableLiveDataOf(1).startWith(2) // will emit 2,1

mutableLiveDataOf(1).reduce { acc, value -> acc + value  } // mutate the previous resulte with the current emission
mutableLiveDataOf(1).reduce(seed = "a") { acc: String, value : Int -> acc + value.toString()  } // emits "a1'
```
Some operators are available via the LiveData KTx library:
``` kotlin
mutableLiveDataOf(1)
    .map { it.toFloat() }
    .switchMap { mutableLiveDataOf(it.toString()) }
    .distinctUntilChanged()
```
The operators below are handy for creating custom operators. Most of the above are created with these.

``` kotlin
mutableLiveDataOf(1).wrap { value, next -> next(value) }

mutableLiveDataOf(1).compose { data -> data.map { it.toString() } } 

mutableLiveDataOf(1).lift { next ->
    // hold (mutable) state here
    { value ->
        next(value)
    }
}
```

## Aggregating LiveData
``` kotlin
mutableLiveDataOf(1).merge(mutableLiveDataOf(1)) // will emit 1, 1
mutableLiveDataOf(1).combine(mutableLiveDataOf("a")) { num, text -> num.toString() + text } // will emit "1a"

mutableLiveDataOf(1).pair(2) // will emit Pair<Int, Int>(1,2)
mutableLiveDataOf(1).window(3) // will emit a list of 3 whenever it has 3 elements.
mutableLiveDataOf(1).buffer(3) // will emit a list of 3 when it has collected 3 elements. 
``` 

## Emitting data

``` kotlin
val data = mutableLiveDataOf()

data.setValues(1,2,3)
data.setValues(listOf(1,2,3))
data.setValues(sequenceOf(1,2,3))
data.setValues { next ->
    next(1)
    next(2)
    next(3)
}
```

## Observing LiveData
The `observe` method of `LiveData` requires Kotlin to always create the `Observer` functional interface. To replace the interface with a lambda, you can use the `subscribe` method.

``` kotlin
fun example(owner: LifecycleOwner) {
    mutableLiveDataOf(1).observe(owner, Observer { println(it) })
    mutableLiveDataOf(1).subscribe(owner) { println(it) }
}
``` 

It is cumbersome the pass a LiveData around, since it requires a LifecycleOwner to make a subscription. To fix this, there is an overload that returns a function that can be called without the need of a LifecycleOwner.

``` kotlin
fun example1(owner: LifecycleOwner) {
    val observe: Observe<Int> = mutableLiveDataOf(1).observe(owner)

    // ... somewhere else the Observer can be supplied    
    observe(Observer { println(it) })
}

fun example2(owner: LifecycleOwner) {
    val subscribe: Subscribe<Int> = mutableLiveDataOf(1).subscribe(owner)

    // ... somewhere else the lambda can be supplied
    subscribe(::println)
}
```

This can be very useful for passing observable data to a ViewModel in the case of events like clicks or text changes. In the sample project are buttons that sort the ViewModel data by using `subscribe` and `combine`.

## Logging
There are operators for executing a side effect before and after calling the subsequent operators.

``` kotlin
mutableLivedataOf(1).beforeEach { println(it) } 
mutableLivedataOf(1).afterEach { println(it) } 
```

## Posting data
The `post` operator will execute all subsequent operation on the main thread.

``` kotlin
mutableLivedataOf(1).post().map { number -> number.toString() }
```

The overload will call `post` after the given callback.

``` kotlin
mutableLivedataOf(1).post { data -> data.map { number -> number.toString() } }
```

# Acknowledgement
Many thanks to [Josef Raska](https://github.com/jraska/livedata-testing) for making LiveData tests a breeze.

# License
   Copyright 2019 Danny Lamarti

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
