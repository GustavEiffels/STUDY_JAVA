import java.util.ArrayList;
import java.util.List;

// 1. Subject 인터페이스 정의
// Subject (관찰 대상)은 Observer를 등록, 해제, 알림 기능을 제공해야 합니다.
interface InventorySubject {
    void registerObserver(InventoryObserver observer);
    void removeObserver(InventoryObserver observer);
    void notifyObservers(); // 모든 등록된 옵저버에게 변경을 알립니다.
}

// 2. Observer 인터페이스 정의
// Observer (관찰자)는 Subject로부터 알림을 받았을 때 처리할 update 메서드를 정의해야 합니다.
interface InventoryObserver {
    // update 메서드는 Subject의 변경된 상태를 전달받을 수 있도록 파라미터를 가집니다.
    void update(int currentStock);
}

// 3. Concrete Subject 클래스: 복싱 글러브 재고
// 실제 재고 상태를 관리하고, 변화 시 등록된 옵저버들에게 알립니다.
class BoxingGloveInventory implements InventorySubject {
    private List<InventoryObserver> observers; // 등록된 옵저버 목록
    private int stockQuantity; // 복싱 글러브 재고 수량

    public BoxingGloveInventory(int initialStock) {
        this.observers = new ArrayList<>();
        this.stockQuantity = initialStock;
        System.out.println("복싱 글러브 재고 시스템 초기화. 현재 재고: " + stockQuantity);
    }

    @Override
    public void registerObserver(InventoryObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("옵저버 등록: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
        System.out.println("옵저버 해제: " + observer.getClass().getSimpleName());
    }

    @Override
    public void notifyObservers() {
        System.out.println("\n--- 재고 변경 알림 ---");
        for (InventoryObserver observer : observers) {
            observer.update(this.stockQuantity); // 모든 옵저버에게 현재 재고 수량을 전달하며 업데이트 요청
        }
        System.out.println("--- 알림 완료 ---\n");
    }

    // 재고 수량을 변경하는 메서드 (Subject의 상태 변경)
    public void purchaseGlove(int quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            System.out.println("복싱 글러브 " + quantity + "개 구매 완료. 남은 재고: " + this.stockQuantity);
            notifyObservers(); // 재고가 변경되었으니 등록된 옵저버들에게 알립니다.
        } else {
            System.out.println("재고가 부족합니다. 현재 재고: " + this.stockQuantity);
        }
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}

// 4. Concrete Observer 클래스 1: 구매 홈페이지 재고 표시
class PurchasePageDisplay implements InventoryObserver {
    private int displayedStock;

    @Override
    public void update(int currentStock) {
        this.displayedStock = currentStock;
        display();
    }

    public void display() {
        System.out.println("[구매 홈페이지] 복싱 글러브 현재 수량: " + displayedStock + "개");
    }
}

// 4. Concrete Observer 클래스 2: 관리자 홈페이지 재고 현황
class AdminPageDisplay implements InventoryObserver {
    private int displayedStock;

    @Override
    public void update(int currentStock) {
        this.displayedStock = currentStock;
        display();
    }

    public void display() {
        System.out.println("[관리자 홈페이지] 복싱 글러브 현황: " + displayedStock + "개 남음");
    }
}

// 4. Concrete Observer 클래스 3: 재고 알림 시스템 (추가 예시)
class StockAlertSystem implements InventoryObserver {
    private static final int ALERT_THRESHOLD = 5; // 재고 알림 임계치

    @Override
    public void update(int currentStock) {
        if (currentStock <= ALERT_THRESHOLD) {
            System.out.println("[재고 알림 시스템] !주의! 복싱 글러브 재고가 " + currentStock + "개로 임계치(" + ALERT_THRESHOLD + "개) 이하입니다. 담당자에게 알립니다.");
            // 실제 시스템이라면 이메일, SMS 발송 등의 로직이 들어갈 수 있습니다.
        }
    }
}

// 메인 실행 클래스
public class ObserverPatternDemo {
    public static void main(String[] args) {
        // 1. Subject 객체 생성 (복싱 글러브 재고 10개로 시작)
        BoxingGloveInventory boxingGloveInventory = new BoxingGloveInventory(10);

        // 2. Observer 객체들 생성
        PurchasePageDisplay purchasePage = new PurchasePageDisplay();
        AdminPageDisplay adminPage = new AdminPageDisplay();
        StockAlertSystem alertSystem = new StockAlertSystem();

        // 3. Observer들을 Subject에 등록
        boxingGloveInventory.registerObserver(purchasePage);
        boxingGloveInventory.registerObserver(adminPage);
        boxingGloveInventory.registerObserver(alertSystem);

        System.out.println("\n--- 첫 번째 구매 발생: 6개 구매 ---");
        boxingGloveInventory.purchaseGlove(6); 

    }
}