'use strict';

describe('Controller Tests', function() {

    describe('WechatShopCardInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWechatShopCardInfo, MockWechatShopCard;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWechatShopCardInfo = jasmine.createSpy('MockWechatShopCardInfo');
            MockWechatShopCard = jasmine.createSpy('MockWechatShopCard');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WechatShopCardInfo': MockWechatShopCardInfo,
                'WechatShopCard': MockWechatShopCard
            };
            createController = function() {
                $injector.get('$controller')("WechatShopCardInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:wechatShopCardInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
