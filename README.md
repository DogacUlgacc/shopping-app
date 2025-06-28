# 🛒 Shopping App – E-Ticaret Backend Uygulaması (Spring Boot + JWT)

Bu proje, temel bir e-ticaret sisteminin arka plan altyapısını sağlayan bir Spring Boot uygulamasıdır. Müşteri kaydı, ürün yönetimi, sepet işlemleri ve güvenli kullanıcı girişi gibi işlevleri kapsar. Tüm korumalı endpoint'lerde JWT tabanlı kimlik doğrulama mekanizması kullanılır.

---

## 📦 Proje Özellikleri

### 🧍 Kullanıcı ve Müşteri İşlemleri
- Kayıt olma ve giriş yapma (register / login)
- JWT token ile kimlik doğrulama
- Müşteri bilgilerini görüntüleme, güncelleme, silme

### 🛍️ Ürün Yönetimi
- Ürünleri listeleme
- Yeni ürün ekleme (admin paneli gibi düşünülebilir)

### 🛒 Sepet (Cart) İşlemleri
- Müşteriye ait bir sepet oluşturulur
- Sepete ürün (CartItem) ekleme
- Sepetten ürün çıkarma
- Sepeti görüntüleme ve toplam tutar hesaplama

### 🔐 Güvenlik
- Spring Security + JWT ile kimlik doğrulama
- Giriş yapmayan kullanıcılar sadece belirli endpoint'lere erişebilir

---

## 🧰 Kullanılan Teknolojiler

| Teknoloji       | Açıklama                           |
|-----------------|------------------------------------|
| Java 17         | Proje dili                         |
| Spring Boot     | Uygulama çatısı                    |
| Spring Security | Kimlik doğrulama ve yetkilendirme |
| JWT (jjwt)      | Token tabanlı kimlik kontrolü      |
| Lombok          | Boilerplate kod azaltma           |
| MySQL           | Geliştirme ve kalıcı veri         |
| Maven           | Build ve bağımlılık yönetimi      |

---


