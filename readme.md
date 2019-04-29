# LiveData utils
When a ViewModel has to work with more complex logic and state, then the default operators  of LiveData (`map`, `switchMap` and `distinctUntilchanged`) are not enough. Hence this repo introduces the most popular operators from RxJava, but then as extension functions on LiveData. Some of the operators are;

- combineLatest
- merge
- filter
- compose
- lift
- reduce
- and more...

If Rx is not part of your toolbelt, then start by reading some of the tests. Since most of the operators are coming from Rx, the [RxMarbles](https://rxmarbles.com/) serve as good documentation.

The library also includes some utility functions for creating and updating LiveData. Try out `livedata` and `mediator` to create a MutableLiveData and a MediatorLiveData respectively.

# Acknowledgement
Many thanks to [Josef Raska](https://github.com/jraska/livedata-testing) for making LiveData testing a breeze.

# License
Copyright 2019 Danny Lamarti

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.