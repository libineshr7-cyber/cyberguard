<c:set var="pageTitle" value="Home"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="hero">
  <p class="eyebrow">Cyber incident reporting portal</p>
  <h1>Report Cyber Incidents.<br><span class="grad">Track Your Case.</span></h1>
  <p class="lead">Securely report phishing, fraud, malware and account compromise, receive a unique Case ID, and follow your case from submission to resolution.</p>
  <div class="actions"><a class="btn" href="${ctx}/report">Report Incident</a><a class="btn btn-ghost" href="${ctx}/track">Track Case</a></div>
</section>

<section>
  <div class="grid g3">
    <article class="card"><div class="icon">1</div><h3>Report</h3><p>Describe what happened, attach screenshots or documents, and submit in minutes.</p></article>
    <article class="card"><div class="icon">2</div><h3>Track</h3><p>Enter your Case ID at any time to see the current stage and investigator updates.</p></article>
    <article class="card"><div class="icon">3</div><h3>Stay Informed</h3><p>Read clear guidance on protecting your accounts, money and devices.</p></article>
  </div>
</section>

<section>
  <h2>Cybersecurity awareness</h2>
  <p class="muted">The most common incidents reported by internet users:</p>
  <div class="grid g4">
    <article class="card"><h3>Phishing</h3><p>Fake emails, SMS or pages that trick you into sharing passwords or OTPs.</p></article>
    <article class="card"><h3>Online Fraud</h3><p>Scams involving fake sellers, investment offers, lotteries or payment requests.</p></article>
    <article class="card"><h3>Account Compromise</h3><p>Someone gains access to your email, social or banking account without permission.</p></article>
    <article class="card"><h3>Malware</h3><p>Harmful software such as ransomware or spyware that damages or spies on your device.</p></article>
  </div>
</section>

<section class="panel">
  <h2>Five habits that prevent most incidents</h2>
  <ul class="ticks">
    <li>Use a unique, long password for every important account and a password manager.</li>
    <li>Turn on two-factor authentication wherever it is offered.</li>
    <li>Never share OTPs, PINs or passwords &mdash; genuine companies never ask for them.</li>
    <li>Check the web address carefully before logging in or paying.</li>
    <li>Keep your phone and computer updated and install apps only from official stores.</li>
  </ul>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
