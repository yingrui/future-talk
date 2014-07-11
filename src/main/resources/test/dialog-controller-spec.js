describe("dialog controller", function(){
    "use strict";

    var scope;

    beforeEach(module(futureTalkCoreModule.name));

    beforeEach(inject(function($rootScope, $controller){
        scope = $rootScope.$new();
        $controller("DialogController", {$scope: scope})
    }));

    it("should init the dialog", function(){

    })
});