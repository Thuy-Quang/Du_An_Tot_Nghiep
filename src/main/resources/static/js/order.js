    var app = angular.module('orderApp', []);
    app.controller('orderController', function ($scope, $http) {
        $scope.orders = [];
        var jwtToken = localStorage.getItem("token");
    
        if (jwtToken) {
            var decodedToken = jwt_decode(jwtToken);
            var userId = decodedToken.userId;
            $scope.userId = userId;  // Lưu userId vào scope để sử dụng trong ng-href
    
            $scope.goToOrderPage = function() {
                window.location.href = '/khachhang/' + $scope.userId;
            };
    
            // Gọi API để lấy đơn hàng của người dùng
            $http.get('/khachhang/' + userId)
                .then(function(response) {
                    $scope.orders = response.data;
                }, function(error) {
                    console.log("Lỗi khi lấy đơn hàng:", error);
                });
        } else {
            console.log("Token không tồn tại");
        }
    });
