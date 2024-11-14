var app = angular.module('cartApp', []);

app.controller('CartController', ['$scope', function ($scope) {
    console.log("vào");
    // Khởi tạo giỏ hàng
    $scope.cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Hàm thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function (sanPham, quantity) {
        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
        let existingProduct = $scope.cart.find(item => item.id === sanPham.id);

        if (existingProduct) {
            // Nếu đã có, tăng số lượng
            existingProduct.quantity += quantity || 1;
        } else {
            // Nếu chưa có, thêm sản phẩm vào giỏ hàng
            $scope.cart.push({
                id: sanPham.id,
                tenSanPham: sanPham.tenSanPham,
                gia: sanPham.gia,
                hinhAnh: sanPham.hinhAnh,
                quantity: quantity || 1
            });
        }

        // Lưu giỏ hàng vào localStorage
        localStorage.setItem('cart', JSON.stringify($scope.cart));

        // Thông báo thành công
        alert("Đã thêm vào giỏ hàng!");
    };

    // Lấy danh sách giỏ hàng từ localStorage
    $scope.getCart = function () {
        return JSON.parse(localStorage.getItem('cart')) || [];
    };
}]);