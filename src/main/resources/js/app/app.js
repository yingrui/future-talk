(function(global){
'use strict';

global.futureTalkCoreModule = angular.module('future-talk', []).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: '/view/main.htm'
            }).
        otherwise({redirectTo:'/'});
}]);

})(window);
