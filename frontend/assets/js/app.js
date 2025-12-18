// ==========================================
// CONFIGURACIÓN DE APIS (MICROSERVICIOS)
// ==========================================
const API_INVENTARIO = 'http://localhost:8081/api/inventario';
const API_PRESTAMOS  = 'http://localhost:8082/api/prestamos';
const API_SOLICITUDES= 'http://localhost:8083/api/solicitudes';
const API_USUARIOS   = 'http://localhost:8084/api/usuarios';
const API_HISTORIAL  = 'http://localhost:8085/api/historial';
const API_BARCODE    = 'http://localhost:8086/api/barcode'; // Puerto 8086

// Variable global para productos en modal préstamo
let productosPrestamoTemp = [];

// ==========================================
// NAVEGACIÓN Y CARGA INICIAL
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    cargarDashboard();
});

document.querySelectorAll('.navbar li').forEach(item => {
    item.addEventListener('click', () => {
        const vista = item.getAttribute('data-vista');

        document.querySelectorAll('.navbar li').forEach(li => li.classList.remove('active'));
        item.classList.add('active');

        document.querySelectorAll('.vista').forEach(sec => sec.classList.remove('visible'));
        document.getElementById(vista).classList.add('visible');

        if (vista === 'inicio') cargarDashboard();
        if (vista === 'inventario') cargarInventario();
        if (vista === 'solicitudes') cargarSolicitudes();
        if (vista === 'prestamos') cargarPrestamos();
        if (vista === 'historial') cargarHistorial();
        if (vista === 'usuarios') cargarUsuarios();
    });
});

const btnNuevo = document.getElementById('btn-nuevo-producto');
if(btnNuevo) {
    btnNuevo.addEventListener('click', async () => {
        await cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
        abrirModal('modal-crear-producto');
    });
}

// ==========================================
// 1. DASHBOARD
// ==========================================
async function cargarDashboard() {
    try {
        const res = await fetch(API_INVENTARIO);
        const productos = await res.json();
        let totalItems = 0;
        productos.forEach(p => totalItems += (p.stock || 0));
        document.getElementById('kpi-total').textContent = totalItems;
        document.getElementById('kpi-disponibles').textContent = totalItems; 
        document.getElementById('kpi-prestados').textContent = "—"; 
    } catch (error) { console.error("Error dashboard", error); }
}

// ==========================================
// 2. INVENTARIO & CÓDIGO DE BARRAS
// ==========================================
async function cargarInventario() {
    const tbody = document.querySelector('#tabla-inventario tbody');
    tbody.innerHTML = '<tr><td colspan="7">Cargando...</td></tr>';

    try {
        const res = await fetch(API_INVENTARIO);
        if(!res.ok) throw new Error("Error API Inventario");
        const data = await res.json();

        console.log("Datos Inventario:", data); // DEBUG

        tbody.innerHTML = '';
        data.forEach(item => {
            // Lógica robusta para encontrar el nombre de la ubicación
            // 1. Intenta item.nombre_ubicacion (si el backend lo aplana)
            // 2. Intenta item.ubicacion.nombre (si el backend devuelve objeto anidado)
            let nombreUbi = item.nombre_ubicacion || '-';
            if (nombreUbi === '-' && item.ubicacion) {
                 nombreUbi = item.ubicacion.nombre || item.ubicacion.nombre_sala || item.ubicacion.nombreSala || '-';
            }

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.codigo}</td>
                <td>${item.nombre}</td> <td>${item.categoria}</td>
                <td>${item.marca}</td>     <td>${item.ubicacion}</td> <td>${item.stock_total}</td>
                <td>
                    <button class="btn-mini" onclick="verCodigoBarras('${item.cod_interno}')" title="Ver Código">🔍 ID</button>
                    <button class="btn-primary" onclick="prepararEdicion(${item.id})">Editar</button>
                    <button class="btn-secondary" onclick="eliminarProducto(${item.id})">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="7">Error al conectar con Inventario</td></tr>';
        console.error(err);
    }
}

// NUEVA FUNCIÓN: Ver Código de Barras
function verCodigoBarras(codigo) {
    if(!codigo || codigo === 'null') return alert("Este producto no tiene código interno válido.");

    // La API devuelve la imagen directamente (MediaType.IMAGE_PNG)
    const urlImagen = `${API_BARCODE}/generate/${codigo}`;
    
    const container = document.getElementById('barcode-container');
    container.innerHTML = `<img src="${urlImagen}" alt="Barcode ${codigo}" style="height: 100px;">`;
    
    document.getElementById('barcode-text').textContent = codigo;
    abrirModal('modal-barcode');
}

// NUEVA FUNCIÓN: Imprimir Código
function imprimirBarcode() {
    const contenido = document.getElementById('barcode-container').innerHTML;
    const texto = document.getElementById('barcode-text').textContent;
    
    const ventana = window.open('', '', 'height=400,width=400');
    ventana.document.write('<html><head><title>Imprimir Código</title></head><body style="text-align:center;">');
    ventana.document.write('<h3>Duoc UC - Pañol</h3>');
    ventana.document.write(contenido);
    ventana.document.write(`<p style="font-family:monospace; font-size: 20px;">${texto}</p>`);
    ventana.document.write('</body></html>');
    ventana.document.close();
    ventana.print();
}

// ... CRUD Inventario (Igual que antes) ...
// ==========================================
// CARGAR COMBOS (CATEGORÍA, MARCA, UBICACIÓN)
// ==========================================
async function cargarCombosProducto(idCat, idMarca, idUbi) {
    try {
        const [resCat, resMarca, resUbi] = await Promise.all([
            fetch(`${API_INVENTARIO}/detalles/categorias`),
            fetch(`${API_INVENTARIO}/detalles/marcas`),
            fetch(`${API_INVENTARIO}/detalles/ubicaciones`)
        ]);

        const dataCat = await resCat.json();
        const dataMarca = await resMarca.json();
        const dataUbi = await resUbi.json();

        // DEBUG: Mira la consola del navegador (F12) para ver qué nombres llegan realmente
        console.log("Categorías:", dataCat); 
        console.log("Marcas:", dataMarca);
        console.log("Ubicaciones:", dataUbi);

        // Llenamos Categorías y Marcas (asumiendo campo "nombre")
        llenarSelect(idCat, dataCat, "id_categoria", "nombre");
        llenarSelect(idMarca, dataMarca, "id_marca", "nombre");

        // Lógica inteligente para Ubicación: busca el campo de nombre correcto
        let labelUbi = "nombre"; // Por defecto
        if (dataUbi.length > 0) {
            if (dataUbi[0].nombre_sala) labelUbi = "nombre_sala";
            else if (dataUbi[0].nombreSala) labelUbi = "nombreSala";
            else if (dataUbi[0].sala) labelUbi = "sala";
        }
        
        // Usar "id_ubicacion" o "id" según lo que llegue
        const idField = (dataUbi.length > 0 && dataUbi[0].id_ubicacion) ? "id_ubicacion" : "id";
        
        llenarSelect(idUbi, dataUbi, idField, labelUbi);

    } catch (e) {
        console.error("Error cargando combos:", e);
        alert("Error cargando listas desplegables. Revisa la consola.");
    }
}

function llenarSelect(idHtml, data, idField, labelField) {
    const select = document.getElementById(idHtml);
    if(!select) return;
    select.innerHTML = `<option value="">Seleccione...</option>`;
    data.forEach(item => {
        const opt = document.createElement("option");
        opt.value = item[idField];
        opt.textContent = item[labelField] || item.nombre;
        select.appendChild(opt);
    });
}

async function guardarProducto() {
    const data = {
        cod_interno: parseInt(document.getElementById('prod-codigo').value),
        nombre_producto: document.getElementById('prod-nombre').value,
        categoria: parseInt(document.getElementById('prod-cat').value),
        marca: parseInt(document.getElementById('prod-marca').value),
        ubicacion: parseInt(document.getElementById('prod-ubi').value),
        cantidad: parseInt(document.getElementById('prod-stock').value)
    };
    if(!data.cod_interno || !data.nombre_producto || !data.categoria) return alert("Complete campos.");

    await fetch(API_INVENTARIO, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    cerrarModal('modal-crear-producto');
    cargarInventario();
}

async function prepararEdicion(id) {
    await cargarCombosProducto('edit-cat', 'edit-marca', 'edit-ubi');
    const res = await fetch(`${API_INVENTARIO}/${id}`);
    const data = await res.json();

    document.getElementById('edit-id').value = data.id || id;
    document.getElementById('edit-codigo').value = data.cod_interno;
    document.getElementById('edit-nombre').value = data.nombre_producto;
    document.getElementById('edit-stock').value = data.stock;
    if(data.categoria_id) document.getElementById('edit-cat').value = data.categoria_id;
    if(data.marca_id) document.getElementById('edit-marca').value = data.marca_id;
    if(data.ubicacion_id) document.getElementById('edit-ubi').value = data.ubicacion_id;

    abrirModal('modal-editar-producto');
}

async function guardarEdicion() {
    const id = document.getElementById('edit-id').value;
    const data = {
        nombre_producto: document.getElementById('edit-nombre').value,
        categoria: parseInt(document.getElementById('edit-cat').value),
        marca: parseInt(document.getElementById('edit-marca').value),
        ubicacion: parseInt(document.getElementById('edit-ubi').value),
        stock: parseInt(document.getElementById('edit-stock').value)
    };
    await fetch(`${API_INVENTARIO}/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    cerrarModal('modal-editar-producto');
    cargarInventario();
}

async function eliminarProducto(id) {
    if(!confirm("¿Eliminar?")) return;
    await fetch(`${API_INVENTARIO}/${id}`, { method: 'DELETE' });
    cargarInventario();
}

// Funciones Auxiliares
async function guardarCategoria() {
    const nombre = document.getElementById('new-cat-name').value;
    await fetch(`${API_INVENTARIO}/detalles/categorias`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ nombre })
    });
    cerrarModal('modal-add-cat');
    cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
}
async function guardarMarca() {
    const nombre = document.getElementById('new-marca-name').value;
    await fetch(`${API_INVENTARIO}/detalles/marcas`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ nombre })
    });
    cerrarModal('modal-add-marca');
    cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
}

// ==========================================
// GUARDAR UBICACIÓN (POST)
// ==========================================
async function guardarUbicacion() {
    const sala = document.getElementById('new-ubi-sala').value;
    const estante = document.getElementById('new-ubi-estante').value;
    const nivel = parseInt(document.getElementById('new-ubi-nivel').value);
    const desc = document.getElementById('new-ubi-desc').value;

    if (!sala || !estante || !nivel) return alert("Complete los campos obligatorios.");

    // Enviamos el objeto con nombres que suelen coincidir con los DTOs
    // Nota: Si tu backend usa DTOs estrictos, asegúrate de que coincidan con 'nombreSala' o 'nombre_sala'
    const body = {
        nombre_sala: sala,    // Intento 1 (snake_case)
        nombreSala: sala,     // Intento 2 (camelCase) - Para asegurar compatibilidad
        estante: estante,
        nivel: nivel,
        descripcion: desc
    };

    try {
        const res = await fetch(`${API_INVENTARIO}/detalles/ubicaciones`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });

        if (res.ok) {
            alert("Ubicación guardada correctamente");
            cerrarModal('modal-add-ubi');
            // Recargar los combos para que aparezca la nueva ubicación
            cargarCombosProducto('prod-cat', 'prod-marca', 'prod-ubi');
        } else {
            const errorText = await res.text();
            console.error("Error al guardar ubicación:", errorText);
            alert("Error al guardar. Revisa la consola para más detalles.");
        }
    } catch (error) {
        console.error(error);
        alert("Error de conexión al guardar ubicación.");
    }
}

// ==========================================
// 3. SOLICITUDES
// ==========================================
async function cargarSolicitudes() {
    const tbody = document.querySelector('#tabla-solicitudes tbody');
    tbody.innerHTML = '<tr><td colspan="6">Cargando...</td></tr>';
    try {
        const res = await fetch(API_SOLICITUDES);
        const data = await res.json();
        const lista = data._embedded ? data._embedded.solicitudResponseDTOList : (Array.isArray(data) ? data : []);
        tbody.innerHTML = '';
        lista.forEach(s => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${s.id}</td>
                <td>${s.rut}</td>
                <td>${s.motivo}</td>
                <td>${s.prioridad}</td>
                <td>${s.estado || 'Pendiente'}</td>
                <td>
                    <button class="btn-primary" onclick="actualizarEstadoSolicitud(${s.id}, 'APROBADA')">Aprobar</button>
                    <button class="btn-secondary" onclick="actualizarEstadoSolicitud(${s.id}, 'RECHAZADA')">Rechazar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) { tbody.innerHTML = '<tr><td colspan="6">Error Solicitudes</td></tr>'; }
}

async function actualizarEstadoSolicitud(id, nuevoEstado) {
    await fetch(`${API_SOLICITUDES}/${id}/estado`, {
        method: 'PUT',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({ estado: nuevoEstado })
    });
    cargarSolicitudes();
}

// ==========================================
// 4. PRÉSTAMOS
// ==========================================
async function cargarPrestamos() {
    const tbody = document.querySelector('#tabla-prestamos tbody');
    tbody.innerHTML = '<tr><td colspan="5">Cargando...</td></tr>';
    try {
        const res = await fetch(API_PRESTAMOS);
        const data = await res.json();
        const lista = data._embedded ? data._embedded.solicitudPrestamoDTOList : (Array.isArray(data) ? data : []);
        tbody.innerHTML = '';
        lista.forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${p.idPrestamo}</td>
                <td>${p.usuarioRut}</td>
                <td>${p.fechaSolicitud}</td>
                <td>${p.estadoSolicitud}</td>
                <td><button class="btn-primary" onclick="devolverPrestamo(${p.idPrestamo})">Devolver</button></td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) { tbody.innerHTML = '<tr><td colspan="5">Error Préstamos</td></tr>'; }
}

async function abrirModalPrestamo() {
    productosPrestamoTemp = [];
    actualizarListaProductosModal();
    
    const res = await fetch(API_INVENTARIO);
    const productos = await res.json();
    const select = document.getElementById('prestamo-prod-select');
    select.innerHTML = '';
    productos.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id; 
        opt.text = `${p.nombre_producto} (Stock: ${p.stock})`;
        select.appendChild(opt);
    });
    abrirModal('modal-nuevo-prestamo');
}

function agregarProductoALista() {
    const select = document.getElementById('prestamo-prod-select');
    const idProd = select.value;
    const nombreProd = select.options[select.selectedIndex].text;
    const cant = parseInt(document.getElementById('prestamo-prod-cant').value);
    if(!idProd) return;

    productosPrestamoTemp.push({ productoIdPrincipal: parseInt(idProd), cantidad: cant, nombreDisplay: nombreProd });
    actualizarListaProductosModal();
}

function actualizarListaProductosModal() {
    const container = document.getElementById('lista-productos-prestamo');
    container.innerHTML = '';
    productosPrestamoTemp.forEach((item, index) => {
        const div = document.createElement('div');
        div.className = 'product-item';
        div.innerHTML = `<span>${item.nombreDisplay} (x${item.cantidad})</span><span class="remove-item" onclick="removerProductoLista(${index})">X</span>`;
        container.appendChild(div);
    });
}
function removerProductoLista(index) {
    productosPrestamoTemp.splice(index, 1);
    actualizarListaProductosModal();
}
async function confirmarPrestamo() {
    const rut = document.getElementById('prestamo-rut').value;
    const motivo = document.getElementById('prestamo-motivo').value;
    const prioridad = document.getElementById('prestamo-prioridad').value;
    if(!rut || productosPrestamoTemp.length === 0) return alert("Ingrese RUT y productos.");

    await fetch(API_PRESTAMOS, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            usuarioRut: parseInt(rut),
            motivoPrestamo: motivo,
            prioridad: prioridad,
            detalles: productosPrestamoTemp.map(p => ({ productoIdPrincipal: p.productoIdPrincipal, cantidad: p.cantidad }))
        })
    });
    cerrarModal('modal-nuevo-prestamo');
    cargarPrestamos();
}
async function devolverPrestamo(id) {
    if(!confirm("¿Confirmar devolución?")) return;
    await fetch(`${API_PRESTAMOS}/${id}/devolver`, { method: 'PUT' });
    cargarPrestamos();
}

// ==========================================
// 5. HISTORIAL & USUARIOS
// ==========================================
async function cargarHistorial() {
    const tbody = document.querySelector('#tabla-historial tbody');
    try {
        const res = await fetch(API_HISTORIAL);
        const data = await res.json();
        const lista = data._embedded ? data._embedded.historialRowList : (Array.isArray(data) ? data : []);
        tbody.innerHTML = '';
        lista.forEach(h => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${h.idDetalle}</td><td>${h.observacion || '-'}</td><td>${h.fecha || '-'}</td>`;
            tbody.appendChild(tr);
        });
    } catch (e) { tbody.innerHTML = '<tr><td colspan="3">Error Historial</td></tr>'; }
}

async function cargarUsuarios() {
    const tbody = document.querySelector('#tabla-usuarios tbody');
    try {
        const res = await fetch(API_USUARIOS);
        const data = await res.json();
        const lista = data._embedded ? data._embedded.usuarioDTOList : (Array.isArray(data) ? data : []);
        tbody.innerHTML = '';
        lista.forEach(u => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${u.rut}-${u.dvRut}</td>
                <td>${u.pnombre} ${u.apPaterno}</td>
                <td>${u.email}</td>
                <td>${u.nombreRol || u.idRol}</td>
                <td><button class="btn-secondary" onclick="eliminarUsuario(${u.rut})">Eliminar</button></td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) { tbody.innerHTML = '<tr><td colspan="5">Error Usuarios</td></tr>'; }
}

function abrirModalUsuario() { abrirModal('modal-crear-usuario'); }
async function guardarUsuario() {
    const data = {
        rut: parseInt(document.getElementById('user-rut').value),
        dvRut: document.getElementById('user-dv').value,
        pnombre: document.getElementById('user-pnombre').value,
        apPaterno: document.getElementById('user-apPaterno').value,
        email: document.getElementById('user-email').value,
        idRol: document.getElementById('user-rol').value
    };
    if(!data.rut || !data.email) return alert("Complete RUT y Email");
    await fetch(API_USUARIOS, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    cerrarModal('modal-crear-usuario');
    cargarUsuarios();
}
async function eliminarUsuario(rut) {
    if(!confirm("¿Eliminar?")) return;
    await fetch(`${API_USUARIOS}/${rut}`, { method: 'DELETE' });
    cargarUsuarios();
}

// Utilidades Modales
function abrirModal(id) { document.getElementById(id).classList.remove('hidden'); }
function cerrarModal(id) { document.getElementById(id).classList.add('hidden'); }