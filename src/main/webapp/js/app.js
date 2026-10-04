document.addEventListener('DOMContentLoaded', function () {
  // mobile navigation
  var toggle = document.querySelector('.nav-toggle'), nav = document.getElementById('nav');
  if (toggle && nav) toggle.addEventListener('click', function () {
    var open = nav.classList.toggle('open');
    toggle.setAttribute('aria-expanded', open);
  });

  // confirmation before destructive actions
  document.querySelectorAll('form[data-confirm]').forEach(function (f) {
    f.addEventListener('submit', function (e) { if (!window.confirm(f.dataset.confirm)) e.preventDefault(); });
  });

  // incident date cannot be in the future
  document.querySelectorAll('input[type=date][data-max-today]').forEach(function (i) {
    i.max = new Date().toISOString().split('T')[0];
  });

  // evidence: type / size / count check (server re-validates everything)
  var file = document.getElementById('evidence');
  if (file) file.addEventListener('change', function () {
    var allowed = ['jpg', 'jpeg', 'png', 'pdf', 'txt'], msg = '';
    if (file.files.length > 3) msg = 'You can attach a maximum of 3 files.';
    Array.prototype.forEach.call(file.files, function (f) {
      var ext = f.name.split('.').pop().toLowerCase();
      if (allowed.indexOf(ext) < 0) msg = 'Only JPG, PNG, PDF or TXT files are allowed.';
      else if (f.size > 5 * 1024 * 1024) msg = 'Each file must be 5 MB or smaller.';
    });
    file.setCustomValidity(msg);
    if (msg) file.reportValidity();
  });

  // register: password confirmation
  var p1 = document.getElementById('password'), p2 = document.getElementById('confirm');
  if (p1 && p2) {
    var check = function () { p2.setCustomValidity(p1.value === p2.value ? '' : 'Passwords do not match.'); };
    p1.addEventListener('input', check); p2.addEventListener('input', check);
  }
});
