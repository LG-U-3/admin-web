    document.addEventListener("DOMContentLoaded", () => {
      const saveButton = document.getElementById("save-group");
      const searchButton = document.getElementById("search-users");
      const searchName = document.getElementById("search-name");
      const searchPhone = document.getElementById("search-phone");
      const searchPlan = document.getElementById("search-plan");
      const resultsBody = document.getElementById("user-search-results");
      const groupCode = document.getElementById("group-code");
      const groupName = document.getElementById("group-name");
      const groupDescription = document.getElementById("group-description");
      const groupIdMatch = window.location.pathname.match(/\\/admin\\/user-groups\\/(\\d+)\\/edit/);
      const groupId = groupIdMatch ? groupIdMatch[1] : null;

      const renderResults = (users) => {
        resultsBody.innerHTML = "";
        if (!users.length) {
          resultsBody.innerHTML = `
            <tr>
              <td class="nowrap" colspan="3" style="text-align: center">검색 결과가 없습니다.</td>
            </tr>
          `;
          return;
        }
        users.forEach((user) => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td class="nowrap">${user.name ?? "-"}</td>
            <td>${user.email ?? "-"}</td>
            <td class="nowrap">${user.phone ?? "-"}</td>
          `;
          resultsBody.appendChild(row);
        });
      };

      searchButton?.addEventListener("click", async () => {
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
        const response = await fetch(`/api/admin/users?${query.toString()}`);
        if (!response.ok) {
          alert("사용자 검색에 실패했습니다.");
          return;
        }
        const data = await response.json();
        renderResults(data.content ?? []);
      });

      saveButton?.addEventListener("click", async () => {
        const payload = {
          code: groupCode.value.trim(),
          name: groupName.value.trim(),
          description: groupDescription.value.trim()
        };
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
    });