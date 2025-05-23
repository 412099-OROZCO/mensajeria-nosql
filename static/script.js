function enviarMensaje() {
  const texto = document.getElementById('mensaje').value;
  fetch('/api/mensajes', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({ usuario: "Carlos", contenido: texto })
  }).then(() => {
    document.getElementById('mensaje').value = '';
    cargarMensajes();
  });
}

function cargarMensajes() {
  fetch('/api/mensajes')
    .then(res => res.json())
    .then(data => {
      const chat = document.getElementById('chat');
      chat.innerHTML = '';
      data.forEach(m => {
        chat.innerHTML += `<div><strong>${m.usuario}:</strong> ${m.contenido}</div>`;
      });
    });
}

setInterval(cargarMensajes, 2000);