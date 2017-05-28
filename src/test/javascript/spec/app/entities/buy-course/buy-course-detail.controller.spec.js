'use strict';

describe('Controller Tests', function() {

    describe('BuyCourse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBuyCourse, MockLearner, MockCourse, MockCoach;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBuyCourse = jasmine.createSpy('MockBuyCourse');
            MockLearner = jasmine.createSpy('MockLearner');
            MockCourse = jasmine.createSpy('MockCourse');
            MockCoach = jasmine.createSpy('MockCoach');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BuyCourse': MockBuyCourse,
                'Learner': MockLearner,
                'Course': MockCourse,
                'Coach': MockCoach
            };
            createController = function() {
                $injector.get('$controller')("BuyCourseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:buyCourseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
