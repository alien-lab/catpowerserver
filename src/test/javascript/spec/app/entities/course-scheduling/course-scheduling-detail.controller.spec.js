'use strict';

describe('Controller Tests', function() {

    describe('CourseScheduling Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCourseScheduling, MockCourse, MockCoach;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCourseScheduling = jasmine.createSpy('MockCourseScheduling');
            MockCourse = jasmine.createSpy('MockCourse');
            MockCoach = jasmine.createSpy('MockCoach');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CourseScheduling': MockCourseScheduling,
                'Course': MockCourse,
                'Coach': MockCoach
            };
            createController = function() {
                $injector.get('$controller')("CourseSchedulingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'catpowerserverApp:courseSchedulingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
