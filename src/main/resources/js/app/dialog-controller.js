(function(module){
    'use strict';

    module.controller('DialogController', ['$scope', function(scope){
        scope.dialog = {
            talks:[]
        };

        scope.talk = "";

        scope.speak = function(){
            if(!!scope.talk && scope.talk !== "") {
                scope.dialog.talks.push(scope.talk);
                scope.talk = "";
            }
        };

    }]);

})(window.futureTalkCoreModule);

