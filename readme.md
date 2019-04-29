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

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.