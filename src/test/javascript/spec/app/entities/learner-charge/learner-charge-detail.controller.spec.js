'use strict';

describe('Controller Tests', function() {

    describe('LearnerCharge Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLearnerCharge, MockLearner, MockCourse, MockCoach, MockCourseScheduling;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLearnerCharge = jasmine.createSpy('MockLearnerCharge');
            MockLearner = jasmine.createSpy('MockLearner');
            MockCourse = jasmine.createSpy('MockCourse');
            MockCoach = jasmine.createSpy('MockCoach');
            MockCourseScheduling = jasmine.createSpy('MockCourseScheduling');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LearnerCharge': MockLearnerCharge,
                'Learner': MockLearner,
                'Course': MockCourse,
                'Coach': MockCoach,
                'CourseScheduling': MockCourseScheduling
            };
            createController = function() {
                $injector.get('$controller')("LearnerChargeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:learnerChargeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
