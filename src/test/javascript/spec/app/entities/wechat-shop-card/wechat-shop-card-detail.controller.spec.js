'use strict';

describe('Controller Tests', function() {

    describe('WechatShopCard Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWechatShopCard, MockCourse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWechatShopCard = jasmine.createSpy('MockWechatShopCard');
            MockCourse = jasmine.createSpy('MockCourse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WechatShopCard': MockWechatShopCard,
                'Course': MockCourse
            };
            createController = function() {
                $injector.get('$controller')("WechatShopCardDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:wechatShopCardUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
