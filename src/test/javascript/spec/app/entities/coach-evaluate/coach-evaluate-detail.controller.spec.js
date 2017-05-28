'use strict';

describe('Controller Tests', function() {

    describe('CoachEvaluate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCoachEvaluate, MockLearner, MockCourse, MockCoach, MockLearnerCharge;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCoachEvaluate = jasmine.createSpy('MockCoachEvaluate');
            MockLearner = jasmine.createSpy('MockLearner');
            MockCourse = jasmine.createSpy('MockCourse');
            MockCoach = jasmine.createSpy('MockCoach');
            MockLearnerCharge = jasmine.createSpy('MockLearnerCharge');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CoachEvaluate': MockCoachEvaluate,
                'Learner': MockLearner,
                'Course': MockCourse,
                'Coach': MockCoach,
                'LearnerCharge': MockLearnerCharge
            };
            createController = function() {
                $injector.get('$controller')("CoachEvaluateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:coachEvaluateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
