    var app = angular.module('cartApp', []);

    app.controller('CartController', function($scope, $http) {
        $scope.cart = {
            order:[],
            items: [],
            colors: [],  // Mảng chứa thông tin màu sắc
            sizes: [],   // Mảng chứa thông tin kích cỡ

            // Hàm thêm sản phẩm vào giỏ hàng
            add(id) {
                var colorId = $scope.selectedColor;  // Màu sắc đã chọn (ID)
                var sizeId = $scope.selectedSize;    // Kích cỡ đã chọn (ID)
                var quantity = $scope.selectedQuantity;  // Số lượng đã chọn

                // Kiểm tra nếu màu sắc và kích cỡ đã được chọn
                if (!colorId || !sizeId) {
                    alert("Vui lòng chọn màu sắc và kích cỡ!");
                    return;
                }

                console.log("ID Sản phẩm:", id);
                console.log("Màu sắc ID:", colorId);
                console.log("Kích cỡ ID:", sizeId);
                console.log("Số lượng:", quantity);

                // Lấy tên màu sắc và kích cỡ từ server
                $scope.getColorName(colorId).then(function(colorName) {
                    $scope.getSizeName(sizeId).then(function(sizeName) {
                        // Tạo đối tượng item với tên màu sắc và kích cỡ
                        var item = {
                            id: id,
                            color: colorName,
                            size: sizeName,
                            soLuong: quantity
                        };

                        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                        var existingItem = $scope.cart.items.find(function(cartItem) {
                            return cartItem.id == id && cartItem.color == colorName && cartItem.size == sizeName;
                        });

                        if (existingItem) {
                            // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
                            existingItem.soLuong += quantity;
                        } else {
                            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
                            $scope.cart.items.push(item);
                        }

                        // Lưu giỏ hàng vào localStorage
                        $scope.cart.saveToLocalStorage();

                        alert("Thêm thành công sản phẩm vào giỏ hàng!");
                    });
                });
            },
           getColorName(colorId) {
                return $http.get(`/HienThi/colors/${colorId}`).then(function(response) {
                    return response.data.name; // Giả sử API trả về { name: 'Đỏ' }
                });
            },

            // Hàm lấy tên kích cỡ từ ID
            getSizeName(sizeId) {
                return $http.get(`/HienThi/sizes/${sizeId}`).then(function(response) {
                    return response.data.name; // Giả sử API trả về { name: 'S' }
                });
            },


            // Lưu giỏ hàng vào localStorage
            saveToLocalStorage: function() {
                var json = JSON.stringify(this.items);
                localStorage.setItem("cart", json);


            },

            // Lấy giỏ hàng từ localStorage khi trang được tải lại
            loadFromLocalStorage: function() {
                var storedCart = localStorage.getItem("cart");
                if (storedCart) {
                    this.items = JSON.parse(storedCart);
                }
            },

            get count() {
                return this.items
                    .map(item => item.soLuong)
                    .reduce((total, soluong) => total += soluong, 0);
            },

            get amount() {
                return this.items
                    .map(item => item.soLuong * item.gia)
                    .reduce((total, soluong) => total += soluong, 0);
            },

            // Hàm lấy màu sắc từ API
            loadColors: function() {
                $http.get(`/HienThi/mau-sac`).then(function(response) {
                    $scope.cart.colors = response.data; // Lưu màu sắc vào mảng
                }).catch(function(error) {
                    console.error("Lỗi khi lấy màu sắc:", error);
                });
            },

            // Hàm lấy kích cỡ từ API
            loadSizes: function() {
                $http.get(`/HienThi/kich-co`).then(function(response) {
                    $scope.cart.sizes = response.data; // Lưu kích cỡ vào mảng
                }).catch(function(error) {
                    console.error("Lỗi khi lấy kích cỡ:", error);
                });
            },
            remove(id){
                var index = this.items.findIndex(item => item.id == id);
                this.items.splice(index,1);
                this.saveToLocalStorage();

            },
            calculateTotal: function() {
                return this.items.reduce((total, item) => {
                    return total + (item.soLuong * item.gia);
                }, 0);
            },


            dathang() {
            var token = localStorage.getItem("token");
            if (token) {
                $http.get("/nguoi-dung/thong-tin", {
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                }).then(function (response) {
                    console.log("Dữ liệu nhận được:", response.data);

                    var user = response.data;

                    // Gán dữ liệu vào các phần tử HTML
                    var hoTenElement = document.getElementById("hoTen");
                    var soDienThoaiElement = document.getElementById("soDienThoai");
                    var emailElement = document.getElementById("email");
                    var diaChiElement = document.getElementById("diaChi");

                    if (hoTenElement) hoTenElement.value = user.hoTen || "";
                    if (soDienThoaiElement) soDienThoaiElement.value = user.soDienThoai || "";
                    if (emailElement) emailElement.value = user.email || "";
                    if (diaChiElement) diaChiElement.value = user.diaChi || "";

                    // Kiểm tra URL hiện tại trước khi chuyển hướng
                    if (!window.location.pathname.includes("/dat-hang")) {
                        window.location.href = "/dat-hang";
                    }
                }).catch(function (error) {
                    console.error("Không thể tải thông tin người dùng:", error);
                });
            } else {
                console.warn("Người dùng chưa đăng nhập.");
                window.location.href = "/dang-nhap/hien-thi";
            }
        },
            // Hàm để giải mã JWT
            parseJwt(token) {
            try {
                const base64Url = token.split('.')[1]; // Lấy phần payload
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Chuẩn hóa Base64
                const jsonPayload = decodeURIComponent(
                    atob(base64)
                        .split('')
                        .map(function (c) {
                            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                        })
                        .join('')
                );
                return JSON.parse(jsonPayload); // Trả về đối tượng JSON
            } catch (e) {
                console.error("Token không hợp lệ:", e);
                return null;
            }
        },

            addToOrder() {
                var token = localStorage.getItem("token");
                if (!token) {
                    alert("Bạn cần đăng nhập để đặt hàng!");
                    window.location.href = "/dang-nhap/hien-thi";
                    return;
                }

                // Giải mã token để lấy thông tin người dùng
                const userInfo = this.parseJwt(token);
                const userId = userInfo ? userInfo.userId : null; // Lấy userId từ payload

                if (!userId) {
                    alert("Không thể xác định ID người dùng từ token!");
                    return;
                }

                if (this.items.length === 0) {
                    alert("Giỏ hàng trống!");
                    return;
                }

                // Dữ liệu đơn hàng
                var orderData = {
                    nguoiDungId: Number(userId), // ID người dùng từ token
                    tongTien: $scope.cart.amount,
                    phuongThucThanhToan: "Chuyển Khoản",
                    sanPhamList: $scope.cart.items.map(item => ({
                        sanPhamId: item.id, // ID sản phẩm
                        soLuong: item.soLuong,
                        giaDonVi: item.gia
                    }))
                };

                // Kiểm tra dữ liệu đơn hàng trước khi gửi
                if (!orderData.sanPhamList || orderData.sanPhamList.length === 0) {
                    alert("Không có sản phẩm trong đơn hàng.");
                    return;
                }

                console.log("Dữ liệu gửi lên server:", orderData);
                console.log("Dữ liệu gửi lên server:", JSON.stringify(orderData, null, 2));

                // Gửi yêu cầu đặt hàng
                $http.post("/HienThiDonHang/dat-hang", orderData, {
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Accept": "application/json" // Đảm bảo client yêu cầu JSON
                    }
                }).then(function (response) {
                    alert(response.data.message); // Thay thế cho chuỗi đơn giản
                    $scope.cart.items = [];
                    $scope.cart.saveToLocalStorage();
                }).catch(function (error) {
                    console.error("Lỗi khi đặt hàng:", error);
                    alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
                });
            }


        };
        // Tải giỏ hàng từ localStorage khi trang được tải lại
        $scope.cart.loadFromLocalStorage();
        // Tải màu sắc và kích cỡ từ API khi trang được tải
        $scope.cart.loadColors();
        $scope.cart.loadSizes();
    });
