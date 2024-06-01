**Challenge Notu**

Product, Customer, Cart ve Order tablolarının ve bu tablolarının miras aldığı bir Base
Entity'nin bulunduğu
Spring Boot ile geliştirilmiş bir proje oluşturun.
Bir müşterinin bir sepeti (cart) ve birden fazla siparişi (order) olabilecek şekilde ilişkilendirme
işlemini gerçekleştirin.
Sepetin ve siparişin toplam fiyat bilgisi her işlemde (sepete ekleme, çıkarma, miktar arttırıp
azaltma) hesaplansın ve sepete kaydedilsin.
Bir müşteri sipariş geçtikten sonra, sipariş içerisindeki ürünlerin fiyatı daha sonradan
güncellendiğinde müşteri satın aldığı anki fiyatı geçmişe yönelik olarak görebilsin. Bunun için
farklı bir tablo tutabilirsiniz. Bu tablo üzerinde ürün, fiyat, miktar gibi bilgiler tutulabilir.
Ürün üzerinde stok takibi yapılsın, bir ürünün stoğu bittikten sonra o üründen daha fazla
sipariş verilemesin.

Yazılması beklenen servisler:
AddCustomer
GetProduct
CreateProduct
UpdateProduct
DeleteProduct
GetCart
UpdateCart
EmptyCart
PlaceOrder
GetOrderForCode
GetAllOrdersForCustomer
AddProductToCart
RemoveProductFromCart
