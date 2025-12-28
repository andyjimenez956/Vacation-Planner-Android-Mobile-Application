

### B4: Excursion title and date
- Excursion entity fields: `title`, `date` (app/src/main/java/.../entities/Excursion.java)
- Excursion detail screen inputs: Title + Date (activity_excursion_detail.xml)
- Excursion list shows title + date (ExcursionAdapter.java)

### B5: Excursion features
- B5a (detailed view): ExcursionDetailActivity.java loads/displays title + date
- B5b (enter/edit/delete): Save/Delete buttons in ExcursionDetailActivity.java
- B5c (date format validation): SimpleDateFormat parse check in ExcursionDetailActivity.java
- B5d (alert): “Set Alert” button schedules notification via AlertReceiver.java
- B5e (date within vacation): validation compares excursion date to parent vacation start/end
