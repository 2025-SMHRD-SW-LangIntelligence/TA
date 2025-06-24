const filterBtn = document.getElementById("filterToggle");
const filterSidebar = document.getElementById("filterSidebar");

// 토글 동작
filterBtn.addEventListener("click", (e) => {
    e.stopPropagation(); // 클릭 이벤트 전파 차단
    const isOpen = filterSidebar.style.right === "0px";
    filterSidebar.style.right = isOpen ? "-500px" : "0px";
});

// 필터 외부 클릭 시 닫기
document.addEventListener("click", function (event) {
    if (
        !filterSidebar.contains(event.target) &&
        event.target !== filterBtn &&
        filterSidebar.style.right === "0px"
    ) {
        filterSidebar.style.right = "-500px";
    }
});

// 탭 클릭 이벤트
document.querySelectorAll(".filter-tab-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        document.querySelectorAll(".filter-tab-btn").forEach(b => b.classList.remove("active"));
        btn.classList.add("active");

        const tab = btn.dataset.tab;
        document.querySelectorAll(".filter-tab-content").forEach(c => c.style.display = "none");
        document.getElementById("filter-" + tab).style.display = "block";
    });
});

const region_list = ['경기도', '서울특별시', '부산광역시', '경상남도',
    '인천광역시', '경상북도', '대구광역시', '충청남도',
    '전라남도', '전북특별자치도', '충청북도', '강원특별자치도',
    '대전광역시', '광주광역시', '울산광역시', '세종특별자치시', '제주특별자치도']
const year_list = ['2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024']
year_list.reverse()
const tr_status_list = ['복구완료', '복구중', '임시복구']

function filter_gen_event(id, list, all_btn_id) {
    let filter_content_div = document.getElementById(id)
    list.forEach(element => {
        filter_content_div.innerHTML += `<label class="filter-tab-label"><input type="checkbox" value="${element}"> ${element}</label>`
    });
    let all_btn = document.getElementById(all_btn_id)
    all_btn.addEventListener("click", e => {
        for (let i = 0; i < filter_content_div.children.length; i++) {
            if (all_btn.checked) {
                filter_content_div.children[i].children[0].checked = true
            } else {
                filter_content_div.children[i].children[0].checked = false
            }
        }
    })
    all_btn.click()
}

// filter 지역
filter_gen_event("filter-region", region_list, "region-tab-label-all")
// filter 연도
filter_gen_event("filter-year", year_list, "year-tab-label-all")
// filter 상태
filter_gen_event("filter-status", tr_status_list, "status-tab-label-all")

