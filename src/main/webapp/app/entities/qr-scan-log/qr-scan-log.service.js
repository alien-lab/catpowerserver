(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('QrScanLog', QrScanLog);

    QrScanLog.$inject = ['$resource', 'DateUtils'];

    function QrScanLog ($resource, DateUtils) {
        var resourceUrl =  'api/qr-scan-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.scanTime = DateUtils.convertDateTimeFromServer(data.scanTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
