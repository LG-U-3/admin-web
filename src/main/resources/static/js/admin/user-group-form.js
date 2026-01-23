    document.addEventListener("DOMContentLoaded", async () => {
      const saveButton = document.getElementById("save-group");
      const searchButton = document.getElementById("search-users");
      const searchName = document.getElementById("search-name");
      const searchPhone = document.getElementById("search-phone");
      const searchPlan = document.getElementById("search-plan");
      const resultsBody = document.getElementById("user-search-results");
      const groupCode = document.getElementById("group-code");
      const groupName = document.getElementById("group-name");
      const groupDescription = document.getElementById("group-description");
      const selectAllCheckbox = document.getElementById("select-all-checkbox");
      const groupIdMatch = window.location.pathname.match(/\/admin\/user-groups\/(\d+)\/edit/);
      const groupId = groupIdMatch ? groupIdMatch[1] : null;

      // Pagination Elements
      const paginationControls = document.getElementById("user-pagination-controls");
      const totalUsersSpan = document.getElementById("total-users");
      const currentRangeSpan = document.getElementById("current-range");
      const prevPageBtn = document.getElementById("prev-page");
      const nextPageBtn = document.getElementById("next-page");
      const pageNumbersContainer = document.getElementById("page-numbers");

      let currentPage = 0;
      const pageSize = 20; // Default page size
      
      // Store selected user IDs
      const selectedUserIds = new Set();
      
      // Store current page users for "Select All" functionality
      let currentUsers = [];

      // If in edit mode, fetch existing user IDs
      if (groupId) {
          try {
              const response = await fetch(`/api/admin/user-groups/${groupId}/user-ids`);
              if (response.ok) {
                  const ids = await response.json();
                  ids.forEach(id => selectedUserIds.add(id));
                  console.log("Loaded existing user IDs:", ids);
              } else {
                  console.error("Failed to load existing user IDs");
              }
          } catch (error) {
              console.error("Error loading existing user IDs:", error);
          }
      }

      const updateSelectAllCheckboxState = () => {
          if (currentUsers.length === 0) {
              selectAllCheckbox.checked = false;
              selectAllCheckbox.disabled = true;
              return;
          }
          selectAllCheckbox.disabled = false;
          const allSelected = currentUsers.every(user => selectedUserIds.has(user.userId));
          selectAllCheckbox.checked = allSelected;
      };

      selectAllCheckbox?.addEventListener("change", (e) => {
          const isChecked = e.target.checked;
          currentUsers.forEach(user => {
              if (isChecked) {
                  selectedUserIds.add(user.userId);
              } else {
                  selectedUserIds.delete(user.userId);
              }
          });
          renderResults(currentUsers);
          console.log("Current selection (Select All):", Array.from(selectedUserIds));
      });

      const renderResults = (users) => {
        resultsBody.innerHTML = "";
        if (!users.length) {
          resultsBody.innerHTML = `
            <tr>
              <td class="nowrap" colspan="4" style="text-align: center">검색 결과가 없습니다.</td>
            </tr>
          `;
          updateSelectAllCheckboxState();
          return;
        }
        users.forEach((user) => {
          const row = document.createElement("tr");
          
          // 1. Checkbox Cell
          const checkboxCell = document.createElement("td");
          checkboxCell.style.textAlign = "center";
          
          const checkbox = document.createElement("input");
          checkbox.type = "checkbox";
          checkbox.value = user.userId;
          checkbox.checked = selectedUserIds.has(user.userId);
          
          checkbox.addEventListener("change", (e) => {
              if (e.target.checked) {
                  selectedUserIds.add(user.userId);
              } else {
                  selectedUserIds.delete(user.userId);
              }
              updateSelectAllCheckboxState(); // Update header checkbox
              console.log("Current selection:", Array.from(selectedUserIds));
          });
          
          checkboxCell.appendChild(checkbox);
          row.appendChild(checkboxCell);
          
          // 2. Name Cell
          const nameCell = document.createElement("td");
          nameCell.className = "nowrap";
          nameCell.textContent = user.name ?? "-";
          row.appendChild(nameCell);

          // 3. Email Cell
          const emailCell = document.createElement("td");
          emailCell.textContent = user.email ?? "-";
          row.appendChild(emailCell);

          // 4. Phone Cell
          const phoneCell = document.createElement("td");
          nameCell.className = "nowrap";
          phoneCell.textContent = user.phone ?? "-";
          row.appendChild(phoneCell);

          resultsBody.appendChild(row);
        });
        
        updateSelectAllCheckboxState();
      };

      const renderPagination = (pageData) => {
        if (pageData.totalElements === 0) {
            paginationControls.style.display = 'none';
            return;
        }
        paginationControls.style.display = 'flex';

        // Update counts
        totalUsersSpan.textContent = pageData.totalElements;
        const start = pageData.pageable.offset + 1;
        const end = Math.min(pageData.pageable.offset + pageData.numberOfElements, pageData.totalElements);
        currentRangeSpan.textContent = `${start}-${end}`;

        // Update buttons
        prevPageBtn.disabled = pageData.first;
        nextPageBtn.disabled = pageData.last;

        // Render page numbers
        pageNumbersContainer.innerHTML = '';
        const totalPages = pageData.totalPages;
        const current = pageData.number;
        
        // Simple pagination logic
        let startPage = Math.max(0, current - 2);
        let endPage = Math.min(totalPages - 1, current + 2);

        for (let i = startPage; i <= endPage; i++) {
            const btn = document.createElement('button');
            btn.className = `btn ${i === current ? 'active' : ''}`;
            btn.type = 'button';
            btn.textContent = i + 1;
            btn.onclick = () => fetchUsers(i);
            pageNumbersContainer.appendChild(btn);
        }

        // Update prev/next button handlers
        prevPageBtn.onclick = () => {
            if (!pageData.first) fetchUsers(current - 1);
        };
        nextPageBtn.onclick = () => {
            if (!pageData.last) fetchUsers(current + 1);
        };
      };

      const fetchUsers = async (page = 0) => {
        const query = new URLSearchParams();
        if (searchName.value.trim()) {
          query.set("name", searchName.value.trim());
        }
        if (searchPhone.value.trim()) {
          query.set("phone", searchPhone.value.trim());
        }
        if (searchPlan.value) {
          query.set("serviceCode", searchPlan.value);
        }
        
        query.set("page", page);
        query.set("size", pageSize);

        try {
            const response = await fetch(`/api/admin/users?${query.toString()}`);
            if (!response.ok) {
              alert("사용자 검색에 실패했습니다.");
              return;
            }
            const data = await response.json();
            currentUsers = data.content ?? []; // Update current users
            renderResults(currentUsers);
            renderPagination(data);
            currentPage = page;
        } catch (error) {
            console.error("Error fetching users:", error);
            alert("사용자 검색 중 오류가 발생했습니다.");
        }
      };

      searchButton?.addEventListener("click", () => {
        fetchUsers(0); // Search starts from page 0
      });

      saveButton?.addEventListener("click", async () => {
        const payload = {
          code: groupCode.value.trim(),
          name: groupName.value.trim(),
          description: groupDescription.value.trim(),
          userIds: Array.from(selectedUserIds)
        };
        
        console.log("Sending payload:", payload);

        const response = await fetch(
          groupId ? `/api/admin/user-groups/${groupId}` : "/api/admin/user-groups",
          {
            method: groupId ? "PUT" : "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
          }
        );
        if (!response.ok) {
          alert("그룹 저장에 실패했습니다.");
          return;
        }
        const data = await response.json();
        if (data?.groupId) {
          window.location.href = `/admin/user-groups/${data.groupId}`;
          return;
        }
        alert("그룹 저장이 완료되었습니다.");
      });

      // Initial fetch to show users immediately
      fetchUsers(0);
    });