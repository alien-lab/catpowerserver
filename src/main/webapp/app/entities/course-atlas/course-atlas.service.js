(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('CourseAtlas', CourseAtlas);

    CourseAtlas.$inject = ['$resource'];

    function CourseAtlas ($resource) {
        var resourceUrl =  'api/course-atlases/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
