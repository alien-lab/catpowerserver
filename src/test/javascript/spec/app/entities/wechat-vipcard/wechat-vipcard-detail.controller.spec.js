'use strict';

describe('Controller Tests', function() {

    describe('WechatVipcard Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWechatVipcard;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWechatVipcard = jasmine.createSpy('MockWechatVipcard');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WechatVipcard': MockWechatVipcard
            };
            createController = function() {
                $injector.get('$controller')("WechatVipcardDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:wechatVipcardUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
