(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('CourseScheduling', CourseScheduling);

    CourseScheduling.$inject = ['$resource', 'DateUtils'];

    function CourseScheduling ($resource, DateUtils) {
        var resourceUrl =  'api/course-schedulings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
