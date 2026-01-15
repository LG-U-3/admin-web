function setupClock() {
  const nowEl = document.getElementById("now");
  if (!nowEl) return;
  const tick = () => {
    const now = new Date();
    const s = now
      .toLocaleString("sv-SE", { timeZone: "Asia/Seoul" })
      .replace("T", " ");
    nowEl.textContent = s;
  };
  tick();
  setInterval(tick, 1000);
}

function applyAlertActions() {
  document.querySelectorAll("[data-alert]").forEach((button) => {
    button.addEventListener("click", () => {
      const message = button.getAttribute("data-alert");
      if (message) {
        alert(message);
      }
    });
  });
}

function renderPreview() {
  const body = document.getElementById("tplBody")?.value ?? "";
  const sample = {
    userName: "홍길동",
    billingMonth: "2026-01",
    usageAmount: "52,000",
    discountAmount: "5,000",
    amount: "47,000",
  };
  let out = body;
  Object.entries(sample).forEach(([key, value]) => {
    out = out.replaceAll(`{${key}}`, value);
  });
  const box = document.getElementById("previewBox");
  if (box) {
    box.textContent = out;
  }
}

function toggleEmailSubject() {
  const type = document.getElementById("tplType")?.value;
  const subjectField = document.getElementById("emailSubjectField");
  if (!subjectField) return;
  subjectField.style.display = type === "EMAIL" ? "block" : "none";
}

function setupTemplateEditor() {
  const previewButton = document.querySelector("[data-action='preview']");
  if (previewButton) {
    previewButton.addEventListener("click", renderPreview);
  }
  const typeSelect = document.getElementById("tplType");
  if (typeSelect) {
    typeSelect.addEventListener("change", toggleEmailSubject);
  }
  renderPreview();
  toggleEmailSubject();
}

function setupQuickSearch() {
  document.addEventListener("keydown", (event) => {
    const isMac = navigator.platform.toUpperCase().includes("MAC");
    const cmdk =
      (isMac && event.metaKey && event.key.toLowerCase() === "k") ||
      (!isMac && event.ctrlKey && event.key.toLowerCase() === "k");
    if (!cmdk) return;
    event.preventDefault();
    const q = prompt(
      "빠른 이동(샘플): settlement | history | retry | templates | user-groups | schedule"
    );
    if (!q) return;
    const map = {
      settlement: "/admin/settlement",
      history: "/admin/messages/history",
      retry: "/admin/messages/retry",
      templates: "/admin/templates",
      "user-groups": "/admin/user-groups",
      schedule: "/admin/schedule",
    };
    const key = q.trim().toLowerCase();
    if (map[key]) {
      window.location.href = map[key];
    } else {
      alert("인식되지 않는 키워드입니다.");
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  setupClock();
  applyAlertActions();
  setupTemplateEditor();
  setupQuickSearch();
});
